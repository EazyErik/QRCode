package qrcodeapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/")
public class QRController {

    @Autowired
    QRService qrService;


    @GetMapping("health")
    ResponseEntity<Void> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("qrcode")
    ResponseEntity<Object> getImage(@RequestParam(required = false,name = "size",defaultValue = "250") Integer size, @RequestParam(required = false,defaultValue = "png") String type, @RequestParam(required = false) String contents, @RequestParam(required = false,defaultValue = "L") String correction) throws IOException {
        System.out.println(type + size +contents +correction);
        final List allowedTypes = new ArrayList<>(List.of("png", "jpeg", "gif"));
        final List allowedCorrectionLevels = new ArrayList(List.of("L", "M", "Q", "H","l","q","m","h"," "));
        if (contents == null || contents.isBlank() || contents.isEmpty()) {
            return new ResponseEntity<>(
                    new ErrorMessage("Contents cannot be null or blank"), HttpStatus.BAD_REQUEST);
        }
        if (size < 150 || size > 350) {
            return new ResponseEntity<>(
                    new ErrorMessage("Image size must be between 150 and 350 pixels"), HttpStatus.BAD_REQUEST);
        }
        if (!allowedCorrectionLevels.contains(correction)) {
            return new ResponseEntity<>(
                    new ErrorMessage("Permitted error correction levels are L, M, Q, H"), HttpStatus.BAD_REQUEST
            );
        }
        if (!allowedTypes.contains(type)) {
            return new ResponseEntity<>(
                    new ErrorMessage("Only png, jpeg and gif image types are supported"), HttpStatus.BAD_REQUEST);
        }
        //todo:ErrorMessage wird nicht angezeigt!


        BufferedImage image = qrService.createQRCodeWithCorrection(contents, size,correction);

        try (var baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, type, baos); // writing the image in the PNG format
            byte[] bytes = baos.toByteArray();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf("image/" + type))
                    .body(bytes);
        } catch (IOException e) {
            // handle the IOEexception
            return null;
        }

    }

}
