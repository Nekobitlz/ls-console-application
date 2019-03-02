Displays the contents of the directory specified as an argument as a sorted list of file names. 

● The -l (long) flag switches the output to a long format in which, in addition to the file name, the rights to execute / read / write in the form of the XXX bit mask, the last modification time and the size in bytes are indicated. 
● The -h (human-readable) flag switches output to a human-readable format (size in kilo, mega or gigabytes, execution rights in the form of rwx). 
● The -r (reverse) flag reverses the output order. 
● The -o (output) flag indicates the name of the file to which the result should be output; if this flag is absent, the result is output to the console. If the file is specified as an argument and not a directory, it displays information about this file.

Command Line: ls [-l] [-h] [-r] [-o output.file] directory_or_file
