package qrcodeapi;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Map;

@Service
public class QRService {

    public BufferedImage createQRCode(String data,int size){
        QRCodeWriter writer = new QRCodeWriter();
        BufferedImage bufferedImage = null;
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size);
             bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        } catch (WriterException e) {
            // handle the WriterException

        }
        return bufferedImage;
    }

    public BufferedImage createQRCodeWithCorrection(String data,int size,String correction){
        QRCodeWriter writer = new QRCodeWriter();

        Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, parseCorrectionLevel(correction));
        BufferedImage bufferedImage = null;
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints);
            bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            // handle the WriterException
        }
        return bufferedImage;
    }

    public ErrorCorrectionLevel parseCorrectionLevel(String correction){
        switch(correction){
            case "L":
                return ErrorCorrectionLevel.L;
            case "M":
                return ErrorCorrectionLevel.M;
            case "Q":
                return ErrorCorrectionLevel.Q;
            case "H":
                return ErrorCorrectionLevel.H;
            default:
                return null;
        }
    }

}
