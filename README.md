
# blizzard
A simple weather application for collaborative work for GADS team-aad-388. 

### Goal ✅

 1. Display the weather information for a given city entered by the user
    based on the <a href ="https://github.com/rafsanjani/blizzard/blob/master/design/Blizzard.xd">
    sample design</a>
 2. Give periodic notifications of weather updates when changes occur for eg, if temp changes from 20 to 25
 3. Automate UI tests using Espresso

### API Details
Weather API is from http://www.openweathermap.org.<br>
We will specifically be making calls to this endpoint:<br>
**https://api.openweathermap.org/data/2.5/weather?q={CITY_NAME}&appid={API_KEY}&units=metric**<br>
where CITY_NAME is the name of the city and API_KEY is your personal API key generated from openweathermap.org.**

An example weather call to Manchester using the API key **40cd2deaa64e7dj8ca3a5aa75f1e0ad0** would therefore look like <br> **https://api.openweathermap.org/data/2.5/weather?q=Manchester&appid=40cd2deaa64e7dj8ca3a5aa75f1e0ad0&units=metric**


#### A sample API call for the weather in London, UK would be <br>
https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02

