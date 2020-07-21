package com.ssafy.sub.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.sub.dto.UserDto;

@Mapper
public interface UserMapper {
	public UserDto login(@Param("uid") String uid, @Param("upw") String upw);
	public int create(UserDto dto);
	public String idcheck(String uid);
	public String nickcheck(String unick);
	public UserDto pwreset(@Param("uid") String uid, @Param("upw") String upw);
	public int pwcheck(@Param("uid") String uid, @Param("upw") String upw);
	public String edcheck(@Param("uemail") String uemail);
}
