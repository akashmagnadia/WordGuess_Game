# WordGuess Game

**How the Game works**
<br/>
This program has a server and client. 
The server can handle at least 10 clients simultaneously. 
First the server is assigned to a port (5555). 
Then a client is assigned an IP address (127.0.0.1), and the same port as the server. 
When the client connects to the server, it is assigned a client number so that server can remember that client. 
The server creates a thread every single client that connects to it and server holds all the client information. 
On the client side, client doesn't have access to the words and all the logic is performed and the server-side, converted back to client-side game information and sent back to the client. 

**Rules of the Game**
<br/>
From the three categories, the client has to select one category. 
Correct guess doesn't count towards six guesses given to the client. 
The client loses the round if it gives six wrong guesses in that particular round. 
The client must guess the correct word before the six guesses run out. 

If the client loses three rounds consecutively, the client loses the game.
If the client is able to guess at least one word from each category, the client wins the game.

**Added Feature(s)**
In this game, I am using a java class to parse Datamuse API to fetch words related to the three categories. In the case when the API response isn't successful the server falls back on words already assigned to each categories.

**How to install this program on your computer** 
<br/>
Download the file for the
<a href = "https://github.com/akashmagnadia/WordGuess_Game/blob/master/out/artifacts/ServerWordGuessSpring2020_jar/ServerWordGuessSpring2020.jar"> server </a>
and the
<a href = "https://github.com/akashmagnadia/WordGuess_Game/blob/master/out/artifacts/ClientWordGuessSpring2020_jar/ClientWordGuessSpring2020.jar"> client</a> 
by opening them in the new tab.

**Instructions to launch and getting started with the game**
<br/>
Download Java at https://www.java.com/en/
Then run the server and client jar file you downloaded above.
