# Rewards point calculator application API

## There are two end points
#### One which accepts the Customer Transactions payload which includes CustomerId, Date of Transaction and Transaction Amont
#### The other which let the Users query Rewards points for a Given Customer in a Month/Year
<br/> 

### Testing Use cases
##### Customer Doesn't exist, expect 404
`curl -H "Accept: application/json" http://localhost:8080/rewards/100/01-2017`

<br/>

##### Customer has not done any Transaction on the queried month/year, expect 0 rewards points
###### Post the Transactions:
`curl -H "Content-Type: application/json" -d '[{"customerId":"200", "transactionAmount":"55", "transactionDate":"2017-01-23"}]' http://localhost:8080/rewards`
###### Query for Rewards
`curl -H "Accept: application/json" http://localhost:8080/rewards/200/02-2017`

<br/>

##### Customer has not done transaction over $50, expect 0 rewards points
###### Post the Transactions:
`curl -X POST -H "Content-Type: application/json" -d '[{"customerId":"101", "transactionAmount":"45", "transactionDate":"2017-01-23"}]' http://localhost:8080/rewards`
###### Query for Rewards
`curl -H "Accept: application/json" http://localhost:8080/rewards/101/01-2017`

<br/>

##### Customer has done single transaction over $50, that's $66, expect 16 rewards points
###### Post the Transactions:
`curl -H "Content-Type: application/json" -d '[{"customerId":"102", "transactionAmount":"66", "transactionDate":"2017-01-23"}]' http://localhost:8080/rewards`
###### Query for Rewards
`curl -H "Accept: application/json" http://localhost:8080/rewards/102/01-2017`

<br/>

##### A single Customer has done single transaction over $50 in a month and two transaction in a different month 
###### $66 on Jan-2017 expect 16 rewards points
###### $125 & 75 on Feb-2017 expect 125 rewards points
###### Post the Transactions:
`curl -H "Content-Type: application/json" -d '[{"customerId":"103", "transactionAmount":"66", "transactionDate":"2017-01-23"}, {"customerId":"103", "transactionAmount":"125", "transactionDate":"2017-02-22"}, {"customerId":"103", "transactionAmount":"75", "transactionDate":"2017-02-23"}]' http://localhost:8080/rewards`
###### Query for Rewards
`curl -H "Accept: application/json" http://localhost:8080/rewards/103/01-2017`
<br/>
`curl -H "Accept: application/json" http://localhost:8080/rewards/103/02-2017`

<br/>

##### Two different Customers has done single transaction, that's $66 & $45 expect 16 & 0 rewards points
###### Post the Transactions:
`curl -H "Content-Type: application/json" -d '[{"customerId":"104", "transactionAmount":"66", "transactionDate":"2017-01-23"}, {"customerId":"105", "transactionAmount":"45", "transactionDate":"2019-10-23"}]' http://localhost:8080/rewards`
###### Query for Rewards
`curl -H "Accept: application/json" http://localhost:8080/rewards/104/01-2017`
<br/>
`curl -H "Accept: application/json" http://localhost:8080/rewards/105/10-2019`

<br/>

##### More than one Customer has done more than one transaction
###### CustomerId 105, five transaction, 2 in each month Jan-2019 & Feb-2017 and 1 in Feb-2021
###### Jan-2019 - 202+50+100=352
###### Feb-2017 - 52+0=52
###### Feb-2021 - 1 points
###### CustomerId 106, 2 transaction in Mar-2021 and 1 in Jun-2021
###### Mar-2021 - 150+50=200
###### Jun-2021 - 850 points
###### Post the Transactions:
`curl -H "Content-Type: application/json" -d '[{"customerId":"105", "transactionAmount":"201", "transactionDate":"2019-01-23"}, {"customerId":"105", "transactionAmount":"125", "transactionDate":"2019-01-20"}, {"customerId":"105", "transactionAmount":"101", "transactionDate":"2017-02-23"}, {"customerId":"105", "transactionAmount":"45", "transactionDate":"2017-02-10"}, {"customerId":"105", "transactionAmount":"51", "transactionDate":"2021-02-10"}, {"customerId":"106", "transactionAmount":"175", "transactionDate":"2021-03-23"}, {"customerId":"106", "transactionAmount":"5", "transactionDate":"2021-03-15"}, {"customerId":"106", "transactionAmount":"500", "transactionDate":"2021-06-23"}]' http://localhost:8080/rewards`

###### Query for Rewards
`curl -H "Accept: application/json" http://localhost:8080/rewards/105/01-2019`
<br/>
`curl -H "Accept: application/json" http://localhost:8080/rewards/105/02-2017`
<br/>
`curl -H "Accept: application/json" http://localhost:8080/rewards/105/02-2021`
<br/>
`curl -H "Accept: application/json" http://localhost:8080/rewards/106/03-2021`
<br/>
`curl -H "Accept: application/json" http://localhost:8080/rewards/106/06-2021`
