# Assignment 1: Create a Spring Boot application

## Create a REST endpoint to execute a dice distribution simulation:
1. Roll 3 pieces of 6-sided dice a total of 100 times. a. \
a. For every roll sum the rolled number from the dice (the result will be between 3 and 18). \
b. Count how many times each total has been rolled.
c. Return this as a JSON structure.
2. Make the number of dice, the sides of the dice and the total number of rolls configurable through query parameters.
3. Add input validation: \
a. The number of dice and the total number of rolls must be at least 1. \
b. The sides of a dice must be at least 4



## Assignment 3: Store the result of the simulation from Assignment 1 in a database

Create a REST endpoint that can query the stored data:
1. Return the total number of simulations and total rolls made, grouped by all existing dice number–dice side combinations. \
a. Eg. if there were two calls to the REST endpoint for 3 pieces of 6 sided dice, once with a total number of rolls of 100 and once with a total number of rolls of 200, then there were a total of 2 simulations, with a total of 300 rolls for this combination.
2. For a given dice number–dice side combination, return the relative distribution, compared to the total rolls, for all the simulations. \
a. In case of a total of 300 rolls, if the sum „3” was rolled 4 times, that would be 1.33%. \
b. If the sum „4” was rolled 3 times, that would be 1%. \
c. If the total „5” was rolled 11 times, that would be 3.66%. Etc...