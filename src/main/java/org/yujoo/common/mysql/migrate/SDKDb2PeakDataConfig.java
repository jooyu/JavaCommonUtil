package org.yujoo.common.mysql.migrate;

import java.util.LinkedHashMap;
import java.util.List;

public class SDKDb2PeakDataConfig {
	public String union_db_ip;
	public String union_user_name;
	public String union_password;
	public String union_db_prefix = "union_users_";
	public int union_db_hash_min = 0;
	public int union_db_hash_max = 4;
	public String union_table_prefix = "tb_openid_playerids_";
	public int union_table_hash_min = 0;
	public int union_table_hash_max = 99;
	public String peak_data_db_ip;
	public String peak_data_user_name;
	public String peak_data_password;
	public String peak_data_db;
	public String peak_deal_db;
	public String peak_data_table_prefix = "user_";
	public List<String> filter_open_id_list;

	public String getUnion_db_ip() {
		return union_db_ip;
	}

	public void setUnion_db_ip(String union_db_ip) {
		this.union_db_ip = union_db_ip;
	}

	public String getUnion_user_name() {
		return union_user_name;
	}

	public void setUnion_user_name(String union_user_name) {
		this.union_user_name = union_user_name;
	}

	public String getUnion_password() {
		return union_password;
	}

	public void setUnion_password(String union_password) {
		this.union_password = union_password;
	}

	public String getUnion_db_prefix() {
		return union_db_prefix;
	}

	public void setUnion_db_prefix(String union_db_prefix) {
		this.union_db_prefix = union_db_prefix;
	}

	public int getUnion_db_hash_min() {
		return union_db_hash_min;
	}

	public void setUnion_db_hash_min(int union_db_hash_min) {
		this.union_db_hash_min = union_db_hash_min;
	}

	public int getUnion_db_hash_max() {
		return union_db_hash_max;
	}

	public void setUnion_db_hash_max(int union_db_hash_max) {
		this.union_db_hash_max = union_db_hash_max;
	}

	public String getUnion_table_prefix() {
		return union_table_prefix;
	}

	public void setUnion_table_prefix(String union_table_prefix) {
		this.union_table_prefix = union_table_prefix;
	}

	public int getUnion_table_hash_min() {
		return union_table_hash_min;
	}

	public void setUnion_table_hash_min(int union_table_hash_min) {
		this.union_table_hash_min = union_table_hash_min;
	}

	public int getUnion_table_hash_max() {
		return union_table_hash_max;
	}

	public void setUnion_table_hash_max(int union_table_hash_max) {
		this.union_table_hash_max = union_table_hash_max;
	}

	public String getPeak_data_db_ip() {
		return peak_data_db_ip;
	}

	public void setPeak_data_db_ip(String peak_data_db_ip) {
		this.peak_data_db_ip = peak_data_db_ip;
	}

	public String getPeak_data_user_name() {
		return peak_data_user_name;
	}

	public void setPeak_data_user_name(String peak_data_user_name) {
		this.peak_data_user_name = peak_data_user_name;
	}

	public String getPeak_data_password() {
		return peak_data_password;
	}

	public void setPeak_data_password(String peak_data_password) {
		this.peak_data_password = peak_data_password;
	}

	public String getPeak_data_db() {
		return peak_data_db;
	}

	public void setPeak_data_db(String peak_data_db) {
		this.peak_data_db = peak_data_db;
	}
	
	public String getPeak_deal_db() {
		return peak_data_db;
	}

	public void setPeak_deal_db(String peak_deal_db) {
		this.peak_deal_db = peak_deal_db;
	}

	public String getPeak_data_table_prefix() {
		return peak_data_table_prefix;
	}

	public void setPeak_data_table_prefix(String peak_data_table_prefix) {
		this.peak_data_table_prefix = peak_data_table_prefix;
	}
	
	public LinkedHashMap<String, Object> getFieldMpas() {
		LinkedHashMap<String, Object> maps = new LinkedHashMap<>();
		maps.put("union_db_ip           ", this.union_db_ip);
		maps.put("union_user_name       ", this.union_user_name);
		maps.put("union_password        ", this.union_password);
		maps.put("union_db_prefix       ", this.union_db_prefix);
		maps.put("union_db_hash_min     ", this.union_db_hash_min);
		maps.put("union_db_hash_max     ", this.union_db_hash_max);
		maps.put("union_table_prefix    ", this.union_table_prefix);
		maps.put("union_table_hash_min  ", this.union_table_hash_min);
		maps.put("union_table_hash_max  ", this.union_table_hash_max);
		maps.put("peak_data_db_ip       ", this.peak_data_db_ip);
		maps.put("peak_data_user_name   ", this.peak_data_user_name);
		maps.put("peak_data_password    ", this.peak_data_password);
		maps.put("peak_data_db          ", this.peak_data_db);
		maps.put("peak_deal_db          ", this.peak_deal_db);
		maps.put("peak_data_table_prefix", this.peak_data_table_prefix);
		return maps;
	}

	public List<String> getFilter_open_id_list() {
		return filter_open_id_list;
	}

	public void setFilter_open_id_list(List<String> filter_open_id_list) {
		this.filter_open_id_list = filter_open_id_list;
	}

}
