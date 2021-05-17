Khubi Shah
20764562 kushah
openjdk version "11.0.8" 2020-07-14
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.8+10)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.8+10, mixed mode)
macOS 10.15.6 (MacBook Pro 2015)
Notes on implementation:
- I am interpreting the file name provided as the full complete file name.
Any extension provided will be concatenated onto the end of the file name.
It does not make sense to me if the user specifies test.txt and requests extensions
.txt and .jpg on the base of "text". The result will be for test.txt.txt and
test.txt.jpg instead.
- Every extension will be treated with a dot in front of it even if it was 
inputted by the user without the dot. If the user sends in "jpg", the program
creates it as looking for the extension ".jpg", and if they send in ".jpg",
the program will look for an ending of "..jpg".# FileFinder
