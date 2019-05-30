package com.konachan.bean;

public class Picture extends BeanModel {
    private String id;

    private String kid;

    private Integer actual_preview_width;	    //实际预览图宽度

    private Integer actual_preview_height;	    //实际预览图高度

    private String author;			            //所有人

    private String creator_id;			        //创建人ID

    private Integer file_size;			        //文件大小

    private String file_url;			        //文件路径

    private String has_children;		        //有无子项目

    private Integer width;			            //宽度

    private Integer height;			            //高度

    private Integer jpeg_file_size;		        //jpeg文件大小

    private Integer jpeg_width;		            //jpeg文件宽度

    private Integer jpeg_height;	            //jpeg文件高度

    private String jpeg_url;			        //jpeg文件路径

    private String md5;

    private Integer preview_height;		        //预览图高度

    private String preview_url;		            //预览图地址

    private Integer preview_width;		        //预览图宽度

    private Integer sample_file_size;		    //样图文件大小

    private Integer sample_height;		        //样图高度

    private String sample_url;		            //样图文件地址

    private Integer sample_width;		        //样图宽度

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public Integer getActual_preview_width() {
        return actual_preview_width;
    }

    public void setActual_preview_width(Integer actual_preview_width) {
        this.actual_preview_width = actual_preview_width;
    }

    public Integer getActual_preview_height() {
        return actual_preview_height;
    }

    public void setActual_preview_height(Integer actual_preview_height) {
        this.actual_preview_height = actual_preview_height;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public Integer getFile_size() {
        return file_size;
    }

    public void setFile_size(Integer file_size) {
        this.file_size = file_size;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getHas_children() {
        return has_children;
    }

    public void setHas_children(String has_children) {
        this.has_children = has_children;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getJpeg_file_size() {
        return jpeg_file_size;
    }

    public void setJpeg_file_size(Integer jpeg_file_size) {
        this.jpeg_file_size = jpeg_file_size;
    }

    public Integer getJpeg_width() {
        return jpeg_width;
    }

    public void setJpeg_width(Integer jpeg_width) {
        this.jpeg_width = jpeg_width;
    }

    public Integer getJpeg_height() {
        return jpeg_height;
    }

    public void setJpeg_height(Integer jpeg_height) {
        this.jpeg_height = jpeg_height;
    }

    public String getJpeg_url() {
        return jpeg_url;
    }

    public void setJpeg_url(String jpeg_url) {
        this.jpeg_url = jpeg_url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Integer getPreview_height() {
        return preview_height;
    }

    public void setPreview_height(Integer preview_height) {
        this.preview_height = preview_height;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public Integer getPreview_width() {
        return preview_width;
    }

    public void setPreview_width(Integer preview_width) {
        this.preview_width = preview_width;
    }

    public Integer getSample_file_size() {
        return sample_file_size;
    }

    public void setSample_file_size(Integer sample_file_size) {
        this.sample_file_size = sample_file_size;
    }

    public Integer getSample_height() {
        return sample_height;
    }

    public void setSample_height(Integer sample_height) {
        this.sample_height = sample_height;
    }

    public String getSample_url() {
        return sample_url;
    }

    public void setSample_url(String sample_url) {
        this.sample_url = sample_url;
    }

    public Integer getSample_width() {
        return sample_width;
    }

    public void setSample_width(Integer sample_width) {
        this.sample_width = sample_width;
    }
}
