package com.ict.healim.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ict.healim.vo.HospitalVO;
import com.ict.healim.vo.JoinVO;
@Repository
public class JoinDAOImpl implements JoinDAO{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	// ȸ������
	@Override
	public int join(JoinVO jvo) {
		return sqlSessionTemplate.insert("member.join", jvo);
	}
	
	// �̸��� �ߺ����� üũ
	@Override
	public int countByEmail(String mberId) {
		return sqlSessionTemplate.selectOne("member.countByEmail", mberId);
	}
	
	// ���������� ��������
	@Override
	public JoinVO getMyPage(String mberid) {
		return sqlSessionTemplate.selectOne("member.myPage", mberid);
	}
	
	// ���������� ������� ����
	@Override
	public int MyPageUpdate(JoinVO jvo) {
		return sqlSessionTemplate.update("member.update", jvo);
	}
	
	// ȸ������ �α� ����
	@Override
	public int joinLog(JoinVO jvo) {
		return sqlSessionTemplate.insert("member.logInsert", jvo);
	}
	
	// ��纴�� �� ��� ��������
	@Override
	public List<HospitalVO> getHospitalPick(String mber_id) {
		return sqlSessionTemplate.selectList("member.getHPick", mber_id);
	}

	// ���� �� ��� ��������
	@Override
	public List<HospitalVO> getNHPick(String mber_id) {
		return sqlSessionTemplate.selectList("member.getNPick", mber_id);
	}

	// �� ��� ����
	@Override
	public int removePick(String mber_id, String h_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mber_id", mber_id);
		map.put("h_id", Integer.parseInt(h_id));
		return sqlSessionTemplate.delete("member.removePick", map);
	}

	// ��纴�� �� ���� ��������
	@Override
	public int getHPickCount(String mber_id) {
		return sqlSessionTemplate.selectOne("member.getHPickCount", mber_id);
	}

	// ���� �� ���� ��������
	@Override
	public int getNPickCount(String mber_id) {
		return sqlSessionTemplate.selectOne("member.getNPickCount", mber_id);
	}
	
	
	
	
}
