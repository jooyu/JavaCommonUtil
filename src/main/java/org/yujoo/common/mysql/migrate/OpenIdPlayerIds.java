package org.yujoo.common.mysql.migrate;

import java.util.Map;

public class OpenIdPlayerIds {
	public String openid;
	public String device_identifier;
	public String channel_id;
	public Integer game_id;
	public String channel_openid;
	public Long player_id;
	
	public OpenIdPlayerIds(Map<String, Object> item) {
		this.openid = item.get("openid").toString();
		this.device_identifier = item.get("device_identifier").toString();
		this.channel_id = item.get("channel_id").toString();
		this.game_id = Integer.valueOf(item.get("game_id").toString());
		this.channel_openid = item.get("channel_openid").toString();
		this.player_id = Long.valueOf(item.get("player_id").toString());
	}

	@Override
	public String toString() {
		return "OpenIdPlayerIds [openid=" + openid + ", device_identifier=" + device_identifier + ", channel_id=" + channel_id
				+ ", game_id=" + game_id + ", channel_openid=" + channel_openid + ", player_id=" + player_id + "]";
	}
	
}
