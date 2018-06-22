# SaliensAuto
Automate Steam's Saliens

First, when you keep reading, there might be some broken English because I'm not a native English speaker. I'm sorry.

I'm still new to Java and programming in general because I'm a new college student with almost no experience and mostly learned things by myself. I made this project just to practice and also contribute to the community if possible. Because of that, this project might be not as good compare to those already out there.

To those who use this, there might be some errors here and there. Please report to me so that I can fix it if possible.

To those who want to edit/contribute to this, because of my lack of experience, I expect the code to be really terrible to read. And I don't use github much either. So please forgive me.

Also this project is inspired alot from [SalienCheat](https://github.com/SteamDatabase/SalienCheat). Big thanks.

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
The zip file included a .bat file. You can straight up open the .bat while to run the program if you extract both file in the zip in the same folder.

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

