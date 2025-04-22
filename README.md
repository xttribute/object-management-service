Use SpringBoot and Mongodb to create a general object management service 
Application can use this endpoint to create db, collections and objects to store application OLTP data

example endpoints
xttribute-dbHandler
﻿

POST
localhost:8080/createCollection
localhost:8080/createCollection
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
json
{"dbName": "xtrStat", "collName": "views"}
POST
localhost:8080/createDB
localhost:8080/createDB
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
json
{"dbName": "xtrStat"}


xttribute-objectHandler
﻿

POST
localhost:8080/newObject
localhost:8080/newObject
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
View More
json
{"dbName": "xtrStructure", "collName": "user", "docContents":"{'name':'alexli','pwd':'lr521178'}", "uKey":"name"}
POST
localhost:8080/matchObject
localhost:8080/matchObject
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
View More
json
{"dbName": "xtrStructure", "collName": "user", "docContents":"{'email':'changzhengli8048@gmail.com','pwd':'lr521178'}", "operator":"and", "returnType":"token"}
POST
localhost:8080/editObject
localhost:8080/editObject
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
View More
json
{"dbName": "xtrObject", "collName": "xttribute", "docContents":"{'_id':'677f7993ae05f76112ebea73','name':'modify'}", "uKey":"_id","updateKey":"name"}
POST
localhost:8080/getOneObject
localhost:8080/getOneObject
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
View More
json
{ "dbName": "xtrObject", "collName": "photo", "docContents": "{'_id':'67e4d1ab9c16ac54ef85716a'}", "uKey":"_id"}
POST
localhost:8080/getObjects
localhost:8080/getObjects
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
json
{
  "dbName": "xtrObject",
  "collName": "xttribute",
  "docContents": "{'uid':'undefined'}",
  "operator": "none",
  "returnType": "list",
  "sortBy": "_id",
  "order": "ASC"
}
POST
localhost:8080/uploadFile
localhost:8080/uploadFile
﻿

Body
form-data
files
/D:/altumforest/brightpoppy_source/brightpoppy_male.png
xid
6789f09cdb2b1d58d82a0a79
type
thumbnail
folder
thumbnail
dbName
xtrObject
collName
xttribute
POST
localhost:8080/updateStats
localhost:8080/updateStats
﻿

Request Headers
Content-Type
application/json
Body
raw (json)
View More
json
{"dbName": "xtrObject", "collName": "photo", "docContents":"{'_id':'67e39d6d9c16ac54ef857169', 'count':1 }",
