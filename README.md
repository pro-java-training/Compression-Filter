# Compression-Filter
Compression Filter and Log Filter Application

    私有类ResponseWrapper用于封装响应对象, GZIPServletOutputStream用于封装PrintWriter或者ServletOutputStream.
    数据首先被写入GZIPOutputStream, 请求完成时, 它将完成压缩并将响应写入封装的ServletOutputStream.