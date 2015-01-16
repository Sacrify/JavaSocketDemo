# JavaSocketDemo

# Introduction:
# 1. Basic Demo, use a lot of static. 
# 2. Use Writer thread and Reader thread.

# TODO:
# 1. ProcessData is in Reader thread, may need to pop back to main thread.

# Test:
# 1. Tested on read. Writer not tested. 


# Problem found:
# 1. Don't use a Reader to read bytes, use an InputStream!

# Per Java Document:
#An InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset. The charset that it uses may be specified by name or may be given explicitly, or the platform's default charset may be accepted.

# Per link: 
# http://stackoverflow.com/questions/27341162/sockets-passing-unsigned-char-array-from-c-to-java
