package com.ict.healim.dao;

import com.ict.healim.vo.MemberVO;

public interface LoginDAO {
	
	
	
	// �α��� 
	public MemberVO LoginChk(MemberVO lvo) throws Exception;
	public int updateLastLoginTime(MemberVO lvo) throws Exception;
}
