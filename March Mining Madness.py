#cd "C:\Users\schepedw\Documents\Courses\CSSE 490\MarchMiningMadness"
import pandas as pd
import numpy as np
import random
from sklearn import preprocessing
from sklearn import tree
from sklearn import cross_validation
from pandas import DataFrame
regular_season_results=pd.read_csv('regular_season_results.csv')
seasons=pd.read_csv('seasons.csv',index_col=0)
allTeams=pd.read_csv('teams.csv')
tourney_results=pd.read_csv('tourney_results.csv')
tourney_seeds=pd.read_csv('tourney_seeds.csv')
tourney_slots=pd.read_csv('tourney_slots.csv')

def getWinLossTotals(season):#Pass 0 to get all seasons
    teams=allTeams.id.unique()
    winLosses=DataFrame(index=teams,columns=['wins','losses','winpct'])
    regSeason=regular_season_results
    if season!=0:
        regSeason=regSeason.ix[regular_season_results.season==season]
    for i in teams:
        winLosses.loc[i][0]=regSeason.ix[regSeason.wteam==i].season.count()
        winLosses.loc[i][1]=regSeason.ix[regSeason.lteam==i].season.count()
    winLosses.winpct=winLosses.wins*1.0/(winLosses.wins+winLosses.losses)
    winLosses=winLosses.fillna(value=0)
    return winLosses

def getTournamentWinLosses(season):
    tourney=tourney_results
    if season!=0:
        tourney=tourney.ix[tourney.season==season]
    teams=tourney.wteam.unique()
    winLosses=DataFrame(index=allTeams.id.unique(),columns=['wins','losses','winpct'])
    for i in teams:
        winLosses.loc[i][0]=tourney.ix[tourney.wteam==i].season.count()
        winLosses.loc[i][1]=tourney.ix[tourney.lteam==i].season.count()
    winLosses.winpct=winLosses.wins*1.0/(winLosses.wins+winLosses.losses)
    winLosses=winLosses.fillna(value=0)
    return winLosses

def getWeightedTotalTournamentAppearances():
    teams=tourney_seeds.team.unique()
    appearances=DataFrame(index=allTeams.id.unique(),columns=['appearances'])
    for i in teams:
        appearances.loc[i][0]=tourney_seeds.ix[tourney_seeds.team==i].season.count()
    appearances=appearances.fillna(value=0)
    return appearances/appearances.max()

def getPrediction(season):#Anything that says weighted goes into a team's overall score
    weightedTourneyAppearances=getWeightedTotalTournamentAppearances()['appearances']#normalized total appearances
    weightedTourneyPerformance=getTournamentWinLosses(season)['winpct']#season tournament win pct
    allTourneyPerformance=getTournamentWinLosses(0)
    weightedAllTourneyWinPct=allTourneyPerformance['winpct']#overall tournament winpct
    weightedAllTourneyWins=allTourneyPerformance['wins']/allTourneyPerformance['wins'].max()#normalized tournament win count
    weightedSeasonWinLosses=getWinLossTotals(season)['winpct']#regular season win pct
    weightedOverallWinPct=getWinLossTotals(0)['winpct']#overall regular season win pct
    totals=weightedSeasonWinLosses+weightedOverallWinPct+weightedAllTourneyWins+weightedAllTourneyWinPct+weightedTourneyPerformance+weightedTourneyAppearances
    totals.sort(ascending=False)
    return totals

def main():
    tourney2010=getPrediction('O')
    tourney2011=getPrediction('P')
    tourney2012=getPrediction('Q')
    tourney2013=getPrediction('R')
    tourney2014=getPrediction('S')
    
