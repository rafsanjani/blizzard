
# blizzard
A simple weather application for collaborative work for GADS team-aad-388. 

### Goal ✅
 Display the weather information for a given city entered by the user

### API Details
Weather API is from http://www.openweathermap.org. We will specifically be making calls to the Endpoint https://api.openweathermap.org/data/2.5/weather?q={CITY_NAME}&appid={API_KEY}&units=metric where CITY_NAME is the name of the city and API_KEY is your personal API key generated from openweathermap.org. 

An example weather call to Manchester using the API key **40cd2deaa64e7dj8ca3a5aa75f1e0ad0** would therefore look like https://api.openweathermap.org/data/2.5/weather?q=Manchester&appid=40cd2deaa64e7dj8ca3a5aa75f1e0ad0&units=metric

A sample API call for the weather in London, UK will be
https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=439d4b804bc8187953eb36d2a8c26a02
