package cn.zjiali.robot.model.response;


import java.util.Map;

/**
 * @author zJiaLi
 * @since 2022-05-28 17:09
 */
public class RequestSongResponse {

    /**
     * 歌曲发送消息
     */
    private String musicMessage;

    /**
     * 歌曲信息
     */
    private MusicInfo musicInfo;

    public String getMusicMessage() {
        return musicMessage;
    }

    public void setMusicMessage(String musicMessage) {
        this.musicMessage = musicMessage;
    }

    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }

    public static class MusicInfo {
        public String title;
        public String desc;
        public String purl;
        public String murl;
        public String jurl;
        public String source;
        public String icon;
        public long appid;
        public Map<String, String> properties;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPurl() {
            return purl;
        }

        public void setPurl(String purl) {
            this.purl = purl;
        }

        public String getMurl() {
            return murl;
        }

        public void setMurl(String murl) {
            this.murl = murl;
        }

        public String getJurl() {
            return jurl;
        }

        public void setJurl(String jurl) {
            this.jurl = jurl;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public long getAppid() {
            return appid;
        }

        public void setAppid(long appid) {
            this.appid = appid;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
