# SaliensAuto
Automate Steam's Saliens

First, when you keep reading, there might be some broken English because I'm not a native English speaker. I'm sorry.

# Features
* No web browser needed. Since this is a **Java** program, **not JavaSCRIPT**. It'll run as a standalone program which cost less resource than javascript's version. Because no browser means no images, assests, sounds... need to be loaded.
* Easier to install. I think? At least I'm sure it's easier than the PHP's one. You just have to install Java which is pretty common nowaday so some of you may have already installed it anyway (especially those minecraft players). With java installed, you can easily open this program by just opening a file I included in the download.
* Pretty much has the same features as other's automation. **Auto join zone/planet, auto repeat, auto kill, invincibility, always max points,...**
* 2 mode to search for zone/planet. 
* - First mode is to search for planet with the most captured rate so you can hop in and spend as much time in those planets to have more chance winning the games before the plannet closed
* - Second mode is to aggressive search for planet with the most XP return (most higher difficulty zone). With this, you'll level up quicker since it'll always automatically join a planet/zone with highest difficulty possible (hard difficulty).

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
         settoken <token> - Set your token. Visit https://steamcommunity.com/saliengame/gettoken to get your token
         setsearchmode 0 - (Default mode) Choose planet with the highest captured rate to have a chance winning games
         setsearchmode 1 - Choose planet with the highest difficulties to get more XP
         start - Start automating
         stop - Stop automating
         exit - What can this do? Idk. Figure it out by yourself.
```
Easy to understand right? 
First you set your token with *settoken*. Then you change the search mode with *setsearchmode* if necessary. And then *start*.
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
First you have to go to [this page](https://steamcommunity.com/saliengame/gettoken). From there you can see something like:
```
"token":"10a654252939d458563215c9fdsa19"
```
Your token is the nonsense path in between two quotes. Here is *10a654252939d458563215c9fdsa19*

# Can I automate multiple accounts with different tokens at the same time with this?
You absolutely can. Just open the program multiple times and make sure to set the token different each time (either by *settoken* command or *\[args]*) then *start* as usual.

Please keep in mind that you still can't open multiple instances for the same account to boost your progress speed. This is only available for botting multiple accounts with different tokens at the same time.
# Acknowledgments
This project is inspired from [SalienCheat](https://github.com/SteamDatabase/SalienCheat). Big thank.
