
#cd "C:\Users\schepedw\Documents\Courses\CSSE 490\Project"

import pandas as pd
import numpy as np
import random
from sklearn import preprocessing
from sklearn import tree
from sklearn import cross_validation
from pandas import DataFrame
regular_season_results=pd.read_csv('regular_season_results.csv')
seasons=pd.read_csv('seasons.csv',index_col=0)
teams=pd.read_csv('teams.csv')
tourney_results=pd.read_csv('tourney_results.csv',index_col=0)
tourney_seeds=pd.read_csv('tourney_seeds.csv',index_col=0)
tourney_slots=pd.read_csv('tourney_slots.csv')
minTeamNumber=teams.id.min()
maxTeamNumber=teams.id.max()

def main():
	getWinLossTotals()

def getWinLossTotals(season):#Pass 0 to get all season data
    winLosses=DataFrame(index=np.arange(minTeamNumber,maxTeamNumber+1),columns=['wins','losses','winpct'])
    regSeason=regular_season_results
    if season!=0:
        regSeason=regular_season_results.ix[regular_season_results.season==season]
    for i in np.arange(minTeamNumber,maxTeamNumber):
        winLosses.loc[i][0]=regSeason.ix[regSeason.wteam==i].season.count()
        winLosses.loc[i][1]=regSeason.ix[regSeason.lteam==i].season.count()
    winLosses.winpct=winLosses.wins*1.0/(winLosses.wins+winLosses.losses)
    return winLosses

def getTournamentWinLosses(season):#Needs to find whether a team was in the tournament in the given season
    teams=tourney_results.wteam.unique()
    winLosses=DataFrame(index=teams,columns=['wins','losses','winpct'])
    tourney=tourney_results
    if season!=0:
        tourney=tourney_results.ix[regular_season_results.season==season]
    for i in teams:
        winLosses.loc[i][0]=tourney.ix[tourney.wteam==i].season.count()
        winLosses.loc[i][1]=tourney.ix[tourney.lteam==i].season.count()
    winLosses.winpct=winLosses.wins*1.0/(winLosses.wins+winLosses.losses)
    return winLosses
	
	

