package seohyun.app.seohyunstay.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageFile {

    public List<String> CreateImage(MultipartFile[] image) throws IOException {

        List<String> list = new ArrayList<>();
        for (MultipartFile eachImage : image) {
            String uploadPath = "/Users/parkseohyun/project/seohyun-stay/src/main/java/seohyun/app/seohyunstay/imageUpload/";
            String originalFileName = eachImage.getOriginalFilename();
            UUID uuid = UUID.randomUUID();
            String savedFileName = uuid.toString() + "_" + originalFileName;
            File file = new File(uploadPath + savedFileName);
            eachImage.transferTo(file);
            list.add(uploadPath + savedFileName);
        }
        return list;
    }
}
