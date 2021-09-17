package com.terly.wopi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terly.wopi.domain.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author terly
 * @date 2021-09-16 15:29
 */
@Slf4j
@RestController
@RequestMapping(value="/wopi")
public class WopiHostContrller {

    @Value("${filePath}")
    private String filePath="D:/Desktop/";

    /**
     * 获取文件流
     * @param name
     * @param response
     */
    @GetMapping("/files/{name}/contents")
    public void getFile(@PathVariable(name = "name") String name, HttpServletResponse response) {
        System.out.println("GET获取文件啦!!!!");
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            // 文件的路径
            String path = filePath + name;
            File file = new File(path);
            // 取得文件名
            String filename = file.getName();
            // 以流的形式下载文件
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();

            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" +filename);
            response.addHeader("Content-Length", "" + file.length());
            toClient =response.getOutputStream();
            //文件类型，设置还是不设置都不影响
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            toClient.write(buffer);
            toClient.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (toClient != null) {
                    toClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("文件输出完成");
        }
    }

    /**
     * 保存更新文件
     * @param name
     * @param content
     */
    @PostMapping("/files/{name}/contents")
    public void postFile(@PathVariable(name = "name") String name, @RequestBody byte[] content) {
        // 文件的路径
        String path = filePath + name;
        File file = new File(path);
        FileOutputStream fop = null;
        try {
            if (!file.exists()) {
                file.createNewFile();//构建文件
            }
            fop = new FileOutputStream(file);
            fop.write(content);
            fop.flush();
            log.debug("------------ save file ------------ ");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件信息
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/files/{name}")
    public FileInfo getFileInfo(@PathVariable(name = "name") String fileName,HttpServletRequest request, HttpServletResponse response) {
        FileInfo info = new FileInfo();
        try {

            if (fileName != null && fileName.length() > 0) {
                // 获取文件名, 防止中文文件名乱码
                File file = new File(filePath + fileName);
                if (file.exists()) {
                    // 取得文件名
                    info.setBaseFileName(file.getName());
                    info.setSize(file.length());
                    //info.setOwnerId("admin");
                    info.setUserCanWrite(true);
                    info.setUserId("admin");
                }else {
                    log.debug("文件不存在");
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            String json =  mapper.writeValueAsString(info);
            log.info(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return info;
    }
}
