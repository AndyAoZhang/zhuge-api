package com.zhonghuipro.zhuge.api;

import com.zhonghuipro.zhuge.api.controller.CallCenterCallbackController;
import com.zhonghuipro.zhuge.api.helper.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
class ZhugeApiApplicationTests {
    @Autowired
    CallCenterCallbackController callCenterCallbackController;

    @Test
    void contextLoads() {
        Map<String, String> params = new HashMap<>();
        String callId="130301340124^117079730123";

        String contentType="normal";
        String content="{\"role\":\"B\",\"words\":\"好，这个这个有问题，真的。\",\"end_offset\":\"21230\",\"begin_time\":\"1659885710\",\"begin_offset\":\"18450\",\"asr_begin_time\":\"1659885727730\",\"record_begin_time\":\"1659885709366\",\"asr_end_time\":\"1659885730510\",\"identity\":\"id2\",\"is_playing\":\"false\",\"play_begin_time\":\"1659885709377\",\"play_end_time\":\"1659885709377\"}";
        Long timestamp = 1659885732000L;

        callCenterCallbackController.rootRequest(params, callId, timestamp, contentType, content);
//        String res = URLDecoder.decode("content_type=normal&content=%7B%22role%22%3A%22B%22%2C%22words%22%3A%22%E5%A5%BD%EF%BC%8C%E8%BF%99%E4%B8%AA%E8%BF%99%E4%B8%AA%E6%9C%89%E9%97%AE%E9%A2%98%EF%BC%8C%E7%9C%9F%E7%9A%84%E3%80%82%22%2C%22end_offset%22%3A%2221230%22%2C%22begin_time%22%3A%221659885710%22%2C%22begin_offset%22%3A%2218450%22%2C%22asr_begin_time%22%3A%221659885727730%22%2C%22record_begin_time%22%3A%221659885709366%22%2C%22asr_end_time%22%3A%221659885730510%22%2C%22identity%22%3A%22id2%22%2C%22is_playing%22%3A%22false%22%2C%22play_begin_time%22%3A%221659885709377%22%2C%22play_end_time%22%3A%221659885709377%22%7D&call_id=130301340124%5E117079730123&timestamp=1659885732000");
//        System.out.println(res);
    }
//    content_type=normal&content={"role":"B","words":"好，这个这个有问题，真的。","end_offset":"21230","begin_time":"1659885710","begin_offset":"18450","asr_begin_time":"1659885727730","record_begin_time":"1659885709366","asr_end_time":"1659885730510","identity":"id2","is_playing":"false","play_begin_time":"1659885709377","play_end_time":"1659885709377"}&call_id=130301340124^117079730123&timestamp=1659885732000


    @Test
    void testCallIn() throws JSONException {
        Map<String, String> params = new HashMap<>();
        String callId="130421293408^117199423408";

        String contentType="callin";
        String content="{\"caller\":\"15201030105\",\"callee\":\"05923391657\",\"partnerId\":\"100000160776001\"}";
        Long timestamp = 1660568049285L;

        String res = callCenterCallbackController.rootRequest(params, callId, timestamp, contentType, content);
        JSONObject resJson = JsonUtils.getJsonObject(res);
        assert Objects.requireNonNull(resJson).getJSONObject("data").getString("dynamic_id") != null;
//        String res = URLDecoder.decode("content_type=normal&content=%7B%22role%22%3A%22B%22%2C%22words%22%3A%22%E5%A5%BD%EF%BC%8C%E8%BF%99%E4%B8%AA%E8%BF%99%E4%B8%AA%E6%9C%89%E9%97%AE%E9%A2%98%EF%BC%8C%E7%9C%9F%E7%9A%84%E3%80%82%22%2C%22end_offset%22%3A%2221230%22%2C%22begin_time%22%3A%221659885710%22%2C%22begin_offset%22%3A%2218450%22%2C%22asr_begin_time%22%3A%221659885727730%22%2C%22record_begin_time%22%3A%221659885709366%22%2C%22asr_end_time%22%3A%221659885730510%22%2C%22identity%22%3A%22id2%22%2C%22is_playing%22%3A%22false%22%2C%22play_begin_time%22%3A%221659885709377%22%2C%22play_end_time%22%3A%221659885709377%22%7D&call_id=130301340124%5E117079730123&timestamp=1659885732000");
//        System.out.println(res);
    }
//    content_type=normal&content={"role":"B","words":"好，这个这个有问题，真的。","end_offset":"21230","begin_time":"1659885710","begin_offset":"18450","asr_begin_time":"1659885727730","record_begin_time":"1659885709366","asr_end_time":"1659885730510","identity":"id2","is_playing":"false","play_begin_time":"1659885709377","play_end_time":"1659885709377"}&call_id=130301340124^117079730123&timestamp=1659885732000


}
