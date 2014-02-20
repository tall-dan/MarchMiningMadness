import pandas as pd
import numpy as np
import random
from sklearn import preprocessing
from sklearn import tree
from sklearn import cross_validation
from pandas import DataFrame
from pandas import Series
from pandas import concat
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
    weightedTourneyPerformance=getTournamentWinLosses(season)['winpct']*5#season tournament win pct
    allTourneyPerformance=getTournamentWinLosses(0)
    weightedAllTourneyWinPct=allTourneyPerformance['winpct']*1.5#overall tournament winpct
    weightedAllTourneyWins=allTourneyPerformance['wins']/allTourneyPerformance['wins'].max()#normalized tournament win count
    weightedSeasonWinLosses=getWinLossTotals(season)['winpct']*2#regular season win pct
    weightedOverallWinPct=getWinLossTotals(0)['winpct']#overall regular season win pct
    totals=weightedSeasonWinLosses+weightedOverallWinPct+weightedAllTourneyWins+weightedAllTourneyWinPct+weightedTourneyAppearances+weightedTourneyPerformance
    totals.sort(ascending=False)
    return totals

def getTournamentSeeds(season):
    # In the intrest of getting things done quicker I wrote everything inside a giant function
    
    # Create the csv file that lists each team and our estimated rankings for them
    #“team”,”rank”
    #511,score

    seeds = tourney_seeds

    if season!=0:
        seeds=seeds.ix[seeds.season==season]
        
    seed_teams = list(seeds['team'].to_dict().values()) # create an array of the seeded teams this season
    ranks = getPrediction(season) # get the rankings of all teams
    seed_ranks = ranks[seed_teams] # only take the rankings of teams which have been seeded
    # TODO: add header to csv
    
    filename='computedTables/seed_ranks'+season+'.csv'
    seed_ranks.to_csv(filename) # write seeded teams with their rankings to csv  
    
    
    # Create the csv file that lists each round 1 slot and the high and low seeded teams playing
    slots = tourney_slots
    if season!=0:
        slots=slots.ix[slots.season==season]
    
    slots = slots.set_index('slot',drop=False) # set index to round (i.e. R1W1) so I can sort off rounds
    slots = slots.drop('season',1) 
    round1_slots = slots.loc[slots['slot'].map(lambda x: x.startswith('R1'))] # eliminate everything that isn't round 1, leaves us with 32 games
    
    seed_locations = seeds.set_index('seed') # set index to seed (i.e. W01) so this is set as key when converting to dictionary
    seed_locations = seed_locations.drop('season',1)
    # this creates two lists of the same length for the seed and the teams seeded to that location
    seed_locations_slots = list(seed_locations['team'].to_dict().keys()) # returns seed (i.e. W01)
    seed_locations_teams = list(seed_locations['team'].to_dict().values()) # returns team (i.e. 511)
    
    # Figure out which teams are seeded in the playin games and figure out who should win the games based on our rankings
    playin_slots = (slots.loc[:'R1W1']).ix[:-1] # select seed games before R1W1, these are the playin games
    playin_teams = playin_slots.replace(to_replace=seed_locations_slots,value=seed_locations_teams) # replace slot values (i.e. W16a) with a team number
    playin_teams = playin_teams.drop('slot',1)
    playin_dict = playin_teams.to_dict()
    
    #Go through the playin games and calculate who we expect to win, then add the round 1 game they should be seeded to into the list of round 1 seeded games
    for key, value in playin_dict['strongseed'].iteritems():
        
        strong_score = ranks.loc[value]
        weak_score = ranks.loc[playin_dict['weakseed'][key]]
        
        if (strong_score >= weak_score):
            seed_locations_slots.append(key)
            seed_locations_teams.append(value)
        else:
            seed_locations_slots.append(key)
            seed_locations_teams.append(playin_dict['weakseed'][key])

    seeds_with_teams = round1_slots.replace(to_replace=seed_locations_slots,value=seed_locations_teams)
    seeds_with_teams = seeds_with_teams.drop('slot',1)    
    csvName='computedTables/seed_slots'+season+'.csv'
    seeds_with_teams.to_csv(csvName,header=False)  

def tourneyResults(season):
    # this will create a csv file with all the slots and which team actually won it
    
    results = tourney_results.drop(['daynum','wscore','lscore','numot'],1).ix[tourney_results.season==season].drop('season',1) # only get win and loosse team for this season
    #print results.head()
    
    slots = tourney_slots.ix[tourney_slots.season==season].set_index('slot',drop=False).drop('season',1) # get strong and week seed for each game in the tournement
    #print slots.head()
    
    seeds = tourney_seeds.ix[tourney_seeds.season==season].set_index('seed').drop('season',1)
    #print seeds.head()
    
    seed_slots = list(seeds['team'].to_dict().keys()) # returns seed (i.e. W01)
    seed_teams = list(seeds['team'].to_dict().values()) # returns team (i.e. 511)
    bracket = slots.replace(to_replace=seed_slots,value=seed_teams)
    #print bracket.head()

    # Playin Games
    playin_teams = (bracket.loc[:'R1W1']).ix[:-1]
    bracket = winner(playin_teams,bracket,results)
    
    # Normal Games
    for current_round in ['R1','R2','R3','R4','R5','R6']:
        r1_teams = bracket.loc[bracket['slot'].map(lambda x: x.startswith(current_round))]
        bracket = winner(r1_teams,bracket,results)
    
    # Final winner
    champ = (bracket.loc['R5YZ':])
    final_game = DataFrame({'slot' : Series(['WIN']),'strongseed' : Series(['R6CH']),'weakseed' : Series([''])})
    final_game = final_game.set_index('slot',drop=False)
    bracket = concat([bracket,final_game])
    bracket = winner(champ,bracket,results)
    
    bracket = bracket.drop('slot',1)
    bracket.ix['WIN']['weakseed']=bracket.ix['WIN']['strongseed']
    filename='computedTables/real_bracket'+season+'.csv'
    bracket.to_csv(filename,header=False)

    
    
def winner(games,bracket,results):
    games_dict = games.to_dict()
    slot = []
    winner = []
    for key, value in games_dict['strongseed'].iteritems():
        #print key,value,games_dict['weakseed'][key]
        stronger = results.ix[results.wteam == int(value)]
        weaker = results.ix[results.wteam == int(games_dict['weakseed'][key])]
        if (stronger.ix[stronger.lteam == int(games_dict['weakseed'][key])]):
            #print key,value,games_dict['weakseed'][key]
            winner.append(str(value))
            slot.append(str(key))
            #print results.ix[results.wteam == int(value)]
        elif (weaker.ix[weaker.lteam == int(value)]):
            #print key,games_dict['weakseed'][key],value
            winner.append(str(games_dict['weakseed'][key]))
            slot.append(str(key))
            #print results.ix[results.wteam == int(games_dict['weakseed'][key])]
        else:
            print "Error"        
    bracket = bracket.replace(to_replace=slot,value=winner)
    return(bracket)
        

def main():
	seasons=['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R']
	for i in seasons:
		print i
		tourneyResults(i)
		getTournamentSeeds(i)
main()