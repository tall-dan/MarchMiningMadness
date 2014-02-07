
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

def getWinLossTotals():#Could take a year as a param
    winLosses=DataFrame(index=np.arange(minTeamNumber,maxTeamNumber+1),columns=['wins','losses'])
    for i in np.arange(minTeamNumber,maxTeamNumber):
        winLosses.loc[i][0]=regular_season_results.ix[regular_season_results.wteam==i].season.count()
        winLosses.loc[i][1]=regular_season_results.ix[regular_season_results.lteam==i].season.count()
    return winLosses

	
	

