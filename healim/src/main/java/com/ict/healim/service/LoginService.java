package com.ict.healim.service;

import com.ict.healim.vo.MemberVO;

public interface LoginService {

	// �α��� 
	public MemberVO LoginChk(MemberVO lvo) throws Exception;
	public int updateLastLoginTime(MemberVO lvo) throws Exception;
}
