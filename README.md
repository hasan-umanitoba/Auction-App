# Auction-App
Auction App for COMP 3350 Summer 2020


## Team members:

    - Camila,Bustos
    - David,Loewen
    - Muhammad Hasan,Saleem
    - Anamelechi,Stanley
    - Risto,Zimbakov
    
## Packages: 
- acceptance
  - AddAuctionTest
  - AddBidTest
  - AddRatingTest
  - ViewAuctionsTest
- RunAllAcceptanceTests
  
- application
  - Services

- business
    - AccessAuctions
    - AccessBids
    - AccessRatings
    - AccessUsers
    - Calculate

- objects
  - enums
    - AuctionType
    - Category
  - Auction
  - Bid
  - Dollar
  - Product
  - Rating
  - User
    
- persistence
  - DataAccess
  - HSQLDatabaseAccessor

- presentation
    - BidActivity 
    - IndivAuctionActivity
    - ListingAdapter
    - MainActivity
    - Messages
    - NewAuctionActivity
    - RatingActivity


Github Repository:[Auction App](https://github.com/Camila-B/Auction-App)


## How the log was stored:

We split the developer tasks into issues on Github and each team member assigned themselves or was assigned problems for on each individual branch. On a pull request from that branch, the team member specified how long a task took them. On approval and merging of the branch into master, the times spent were put into a table with entries that specify the issue solved and how long it took. This table can be found in the log.txt file.
                      

## Implementation Features:
- Firstly, we created a local copy of the database (app/database/script 
  backup)

- We also added acceptance testing that cover the 4 big stories in our application that include adding an auction properly, being able to add bids to auctions, viewing all auctions and being able to filter and select auctions, and the new big story we implemented this iteration, being able to rate closed auctions that a user has won.(src/androidTest/java/comp3350/auctionapp/tests/acceptance). 
  
- We also added integration testing for all of the business seams that checks across the many seams of the project such as accessing users validity and invalidly, accessing auctions validity and invalidy, accessing bids validity and invalidly, as well as accessing valid and invalidy ratings.(src/test/java/comp3350/auctionapp/tests/integration).
  
- As mentioned above, we also re-added the accessRatings business class so we can properly retrieve what ratings a seller has and also update that sellers rating as well to name a few.
- We also needed a new object for the rating so that was created as well. This can access anything that has to do with a rating such as getting the written portion of the rating or getting a value to the rating ( between 0 and 5).(src/main/java/comp3350/auctionapp/businesss).
  
- In the persistence layer we changed how we access data and rearranged it so the stubdatabase can only be accessed in the testing folder since it is only a part of testing after all and for the production of our project we would not include the stub database. This is why we moved it.(src/main/java/comp3350/auctionapp/persistence).
  
- We also created a DataAccess class within the persistance layer that acts as an interface to retrieve all of the methods, safely, from the database to ensure we only retrieve what we intend to retrieve (users, ratings, etc).(src/main/java/comp3350/auctionapp/persistence)
  
- The navigation header also needed to be updated so we could give the user the opportunity to rate an auction once two criteria were met, the auction  is closed and that particular user have won it.(src/main/java/comp3350/auctionapp/presentation)
  

- Within the presentation layer we added a ratings activity class which needed all the logic and the .xml file to be implemented with it as well.  This allows the user, once within their winning auctions tab, to click on any won auction and review the seller experience with a star rating out of 5 as well as a small description of why they gave that rating. (src/main/java/comp3350/auctionapp/persentation)

**NOTE:** To ensure proper running of acceptance tests please use the following reccommended settings: https://developer.android.com/training/testing/espresso/setup#set-up-environment

## Tested On
The auction app was tested on the Nexus 7 API 23 emulator, and on the samsung S9+.



## Environment Used:

  The auction app runs on the JVM environment.




