package com.codve;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class CompressionFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (((HttpServletRequest) request).getHeader("Accept-Encoding")
                .contains("gzip")) {
            ResponseWrapper wrapper =
                    new ResponseWrapper((HttpServletResponse) response);
            try {
                chain.doFilter(request, wrapper);
            } finally {
                try {
                    wrapper.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Encoding not requested.");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

    private static class ResponseWrapper extends HttpServletResponseWrapper {
        private GZIPServletOutputStream outputStream;
        private PrintWriter writer;

        public ResponseWrapper(HttpServletResponse request) {
            super(request);
        }

        @Override
        public synchronized ServletOutputStream getOutputStream()
            throws IOException {
            if (this.writer != null) {
                throw new IllegalStateException("getWriter() already called.");
            }
            if (this.outputStream == null) {
                this.outputStream =
                        new GZIPServletOutputStream(super.getOutputStream());
            }
            return this.outputStream;
        }
    }

    @Override
    public synchronized PrintWriter getWriter() throws IOException {
        if (this.writer == null && this.outputStream != null) {
        }
    }

    private static class GZIPServletOutputStream extends ServletOutputStream {
        private final ServletOutputStream servletOutputStream;
        private final GZIPOutputStream gzipOutputStream;

        public GZIPServletOutputStream(ServletOutputStream servletOutputStream)
                throws IOException {
            this.servletOutputStream = servletOutputStream;
            this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);
        }

        @Override
        public boolean isReady() {
            return this.servletOutputStream.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            this.servletOutputStream.setWriteListener(writeListener);
        }

        @Override
        public void write(int b) throws IOException {
            this.gzipOutputStream.write(b);
        }

        @Override
        public void close() throws IOException {
            this.gzipOutputStream.close();
        }

        @Override
        public void flush() throws IOException {
            this.gzipOutputStream.flush();
        }

        public void finish() throws IOException {
            this.gzipOutputStream.finish();
        }

    }

}