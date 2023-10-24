package com.zpmc.analysis.service.impl;

import com.google.code.kaptcha.Producer;
import com.zpmc.analysis.dto.ValidateCodeDto;
import com.zpmc.analysis.service.ValidateCodeService;
import com.zpmc.analysis.utils.RedisUtils;
import com.zpmc.analysis.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 验证码实现处理
 *
 * @author ruoyi
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Resource
    private Producer captchaProducer;

    @Resource
    private Producer captchaProducerMath;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 生成验证码
     * @return
     */
    @Override
    public ServerResponse createCapcha(String oldUuid) throws IOException {
        if (oldUuid != null) {
            redisUtils.del("VALIDATE_CODE_" + oldUuid);
        }
        // 保存验证码信息
        String uuid = UUID.randomUUID().toString().substring(0, 8); //生成唯一UUID
        String verifyKey = "VALIDATE_CODE_" + uuid; //作为redis存储的键
        String capStr = null, code = null;  //验证码字符串，验证码
        BufferedImage image = null;

        String capText = captchaProducerMath.createText();
        //打印结果：captchaProducerMath.createText()初始值---8-5=?@3
        capStr = capText.substring(0, capText.lastIndexOf("@")); //  lastIndexOf  返回指定字符在此字符串中最后一次出现处的索引
        // captchaProducerMath.createText()是创建一个数学字符串  比如 8-5=?@3
        // 字符串根据 @ 进行分割
        //  要显示的图片为 @ 前面的字符串生成的图片
        //  验证码应该为 @ 后面的字符串生成的字符
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
        //通过这个 数学验证码生产者 ，根据 capStr ，就是 8-5=?  给BufferedImage对象赋值
        /**
         * 最终capStr就是根据验证码生成的编码
         */
        redisUtils.set(verifyKey, code, 5, TimeUnit.MINUTES);
        // 转换流信息写出
        /**
         * 字节数组输出流 在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中。
         * FastByteArrayOutputStream内部实现由一个LinkedList<byte[]>组成，每一次扩容中分配一个数组的空间，
         * 并当该数据放入到List中。需要分配的数组长度为调用FastByteArrayOutputStream的write方法决定。
         * 而ByteArrayOutputStream内部实现为一个数组每一次扩容需要重新分配空间并将数据复制到新数组中，
         * 这就是FastByteArrayOutputStream比ByteArrayOutputStream主要区别。
         */
        // 快速字节数组输出流
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        //
        /**
         * 使用支持给定格式的任意 ImageWriter 将一个图像写入 File。如果已经有一个 File 存在，则丢弃其内容。
         * im - 要写入的 RenderedImage。
         * formatName - 包含格式非正式名称的 String。
         * output - 将在其中写入数据的 File。
         * 如果没有找到合适的 writer，则返回 false。
         */
        //显然是把captchaProducerMath创建的图像image 以jpg格式 写入 输出流
        //这样做的目的是 为了接下来把图像转成字符
        //是 图像 转 图像字符传
        ImageIO.write(image, "jpg", os);
        ValidateCodeDto validateCodeDto = new ValidateCodeDto();
        validateCodeDto.setUuid(uuid);
        // 图像字符串 通过Base64编码传输
        // Base64编码是从二进制到字符的过程，可用于在HTTP环境下传递较长的标识信息。采用Base64编码具有不可读性，需要解码后才能阅读。
        // 为了加密
        validateCodeDto.setImg(Base64.getEncoder().encodeToString(os.toByteArray()));
        System.out.println("验证码内容" + capStr);
        System.out.println("验证码结果" + code);
        return ServerResponse.init().data(validateCodeDto);
    }

    /**
     * 校验验证码
     */
    @Override
    public Boolean checkCapcha(String code, String uuid) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        if (StringUtils.isEmpty(uuid)) {
            return false;
        }

        //根据传来的UUID，拼接上 验证码存储在Redis中的键的前缀，得到这个用户 在 redis 中 验证码 对应的键
        String verifyKey = "VALIDATE_CODE_" + uuid;
        // 根据这个键，在Redis中获取对应的存储的值
        String captcha = (String) redisUtils.get(verifyKey);
        //Redis 删除这个记录
        redisUtils.del(verifyKey);
        //如果传来的验证码，和后端根据UUID拼接，在Redis中存储的验证码，在忽略大小写后，不一样
        //说明验证码错误，抛出自定义异常 验证码错误
        //否则不做处理，上个方法没有捕捉到内部方法有异常抛出，正常执行，即放行。
        if (!code.equalsIgnoreCase(captcha)) {
            return false;
        }
        return true;
    }

    public void setCaptchaProducerMath(Producer captchaProducerMath) {
        this.captchaProducerMath = captchaProducerMath;
    }

    /**
     * 随机生成指定长度字符串验证码
     *
     * @param length 验证码长度
     */
    public String generateVerifyCode(int length) {
        String strRange = "1234567890";
        StringBuilder strBuilder = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            char ch = strRange.charAt((new Random()).nextInt(strRange.length()));
            strBuilder.append(ch);
        }
        return strBuilder.toString();
    }
}