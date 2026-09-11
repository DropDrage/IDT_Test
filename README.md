# Test for In DebiT inc.
## Description
**Technical requirements**: looks good on tablets, Jetpack Compose, Modularization (UI, Domain, Data. Also, random data is taken from the data layer), unit tests.
### First screen:
Two fields in which we enter the number of rows and columns. (maximum limit - 6 columns and 1000 rows)

### Second screen: 
We build a table, the size of which is specified on the first screen. We load random data into this table (data format is string format).  
Single click on a cell should change the color of the cell (one click changes the color to green, another click returns the color back). Only one cell can be green.  
Double click allows to change the data in the cell. 

## Result
Rejected:
1. Crash on Generate in initial state of first screen (missed after logic refactoring, wasn't tested after, fixed in 5f0ee63)
2. Isn't split by layers (?)
