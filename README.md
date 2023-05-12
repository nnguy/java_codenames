# java_codenames

To run this, first run Codenames.java. 

When Codenames.java is opened, you have two options. 
1. Play with custom words (you will be prompted to upload a file of at least 25 words, with each word on a new line.)
2. Play with hard-coded words (pick from an hard-coded array of over 500 words). 

After you choose one of the options, run Spymaster.java. 

Playing the game: 
1. The Codenames-Team window will indicate which team begins.  
2. The Spymaster for that team will view the Codenames-Spymaster window That Spymaster begins by typing a clue that is related to the codewords that have their team's color, choosing a number to tell the team how many words they should seek, and pressing Submit. 
3. The starting team will choose a word that is associated with that codeword by clicking on that word. One of 4 outcomes will happen.
-The team selects one of their own codewords. In this case, the team can try to select another word or end their turn by pressing the Pass button.
-The team selects one of the codewords for the other team. In this case, the team’s turn ends and the other team’s turn begins. 
-The team selects a non-codeword that automatically ends that team’s turn and begins the other team’s turn. 
-The team selects the death word that immediately ends the game and makes the other team win. 
4. The other team begins their turn and that team's Spymaster performs step 2, and their team will perform step 3. 
5. The game ends when one team finds all of their codewords, or if one team reveals the assassin's codeword. 
6. The Codenames-Team window will indicate the winner. 