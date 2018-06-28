# SaliensAuto
Automate Steam's Saliens

First, when you keep reading, there might be some broken English because I'm not a native English speaker. I'm sorry.

If you need helps, just post on [this Reddit thread](https://www.reddit.com/r/Saliens/comments/8t2g0w/java_another_salienss_botting_program_but_in_java/) or message my reddit account (the one posted the thread) because I'm on reddit most of my time. You can create an issue here on github too but I don't check it that often.

If you want to automate 24/24 without keeping your PC on 24/24, read far below in the *Heroku* section. Quite complicated though.
# Features
* No web browser needed. Means less resources used and your PC will run faster.
* Easy to install. Since Java is so common nowaday. Some of you might have installed Java already (especially those minecraft players).
* Pretty much has the same features as other's automation. **Auto join zone/planet, auto repeat, auto kill, invincibility, always max points, focus specific planet/zone...**
* 3 mode to search for zone/planet. 
  - A mode to search for planet with the closest completion (most captured rate) to let you finish of planets to let new planets appear.
  - Another mode to aggressive search for planet with the most XP return (most higher difficulty zones). Use this mode to get the most XP.
  - And another mode to focus on specific planet/zone. Pretty much explained itselft. Use this when you want to play in specific planet or zone. 

# Download
Go here: https://github.com/KickVN/SaliensAuto/releases

Grab the first SaliensAuto.zip in sight.

# Prerequisite
Since this is a Java program so obviously you need Java to be able to run it.

[Download here](https://java.com/en/download/) and then install it.

# How to run this program on windows
Make sure you have already installed Java. Then you have to download from the link above and extract the zip file to somewhere you'd like.

If you're familiar with java before, just run it the way you like.

In case you don't know how:

## Easiest way
The zip file included a .bat file. You can straight up open the .bat to run the program if you extract both file in the zip in the same folder.

There is a section below if you want to understand what is a .bat file or want to customize it.

## Use cmd.exe
Open cmd.exe and run a command with this structure:
```
java -jar <path_of_SaliensAuto> [args]
```
Where *<path_of_SaliensAuto>* is the path to your SaliensAuto.jar file. For example: C:\\Download\SaliensAuto.jar

And *\[args]* is not required. You can just remove it. I'll talk about what this is below.
## Use .bat file
Create a file that end with *.bat*. Edit that file and write as:
```
java -jar <relative_path_of_SaliensAuto> [args]
```
Where *<relative_path_of_SaliensAuto>* is the path to your SaliensAuto.jar file that's relatived to the path of *.bat* file. For example, if you placed the *.bat* file in the same folder as the SaliensAuto.jar file then you just have to write *SaliensAuto.jar* here.

And *\[args]* the same as above, not required.

## Anyway else?
Yes there are some ways else but these are the basic one. Bear with it.

# On linux/mac?
I've never tried linux or mac before. So, I'm sorry but I can't help you here.

# Commands
After you openned up the program, you will see a list of commands:
```
 Commands List:
       settoken <token> - Set your token. Visit https://steamcommunity.com/saliengame/gettoken to get it.
       setsearchmode <0/1/2> - Set the search mode.
                  Set to 0 to search for highest captured rate planet.
                  Set to 1 (default) to search for planet with most XP reward.
                  Set to 2 to only choose focused planet.
       focusplanet <planet_ID> - Choose planet to focus when search mode is 2. Use planetsinfo command to get planets' ID.
       planetsinfo - Show brief info of all active planets
       focuszone <zone_position> - (Optional) Choose a zone to focused on when search mode is 2.
                   position can be a number start from 1. For example: 60 means 60th zone when counting left to right, top to bottom
                   or can be <row>,<column>. For example: 3,5 means zone in row 3 and column 5.
       changegroup <groupid> - Change the group you represent. ID 33035916 is /r/saliens group.
       start - Start the automating process
       stop - Stop the automating process
       exit - Exit the program
```
Easy to understand right? 


First, you set your token with *settoken*. Then, you can also change the search mode with *setsearchmode* and some more followed commands if necessary. And then *start*.

If you don't know how to get the token, far below I will write a guide.

# What is \[args]
\[args] are 3 arguments that you can use to set some default value.
First argument is the *token*. 

Second is the search mode. The value is either 1 or 0. What is this search mode? It is described in the commands section above.

Third is to start the automation right after you open the program or not. If yes then write basically anything. Otherwise, don't include this and the automation will only start after you typed the *start* command.

Examples launching with args:
```
java -jar <path_of_SaliensAuto> 10a654252939d458563215c9fdsa19
java -jar <path_of_SaliensAuto> 10a654252939d458563215c9fdsa19 1
java -jar <path_of_SaliensAuto> 10a654252939d458563215c9fdsa19 0 start
```
# How to get token?
First, make sure you have logged in [steam's website](https://steamcommunity.com/login). 

Then, you have to go to [this page](https://steamcommunity.com/saliengame/gettoken). From there you can see something like:
```
"token":"10a654252939d458563215c9fdsa19"
```
Your token is the nonsense path in between two quotes. Here is *10a654252939d458563215c9fdsa19*

# Can I automate multiple accounts with different tokens at the same time with this?
You absolutely can. Just open the program multiple times and make sure to set the token different each time (either by *settoken* command or *\[args]*) then *start* as usual.

Please keep in mind that you still can't open multiple instances for the same account to boost your progress speed. This is only available for botting multiple accounts with different tokens at the same time.

# Automate with Heroku
Read more here: https://github.com/KickVN/SaliensAuto/wiki/Heroku

# Acknowledgments
This project is inspired from [SalienCheat](https://github.com/SteamDatabase/SalienCheat). Big thank.
