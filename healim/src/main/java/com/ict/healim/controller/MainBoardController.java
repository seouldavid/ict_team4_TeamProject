package com.ict.healim.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.ict.healim.service.MainBoardService;
import com.ict.healim.vo.HospitalVO;
import com.ict.healim.vo.MainBoardPagingVO;
import com.ict.healim.vo.MainBoardVO;
import com.ict.healim.vo.MemberVO;
import com.ict.healim.vo.SearchVO;
import com.ict.healim.vo.SessionUserVO;

@Controller
public class MainBoardController {
	@Autowired
	private MainBoardService mainBoardService;
	



	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/mainBoard")
	public ModelAndView mainBoard(Model model,SearchVO scvo) {
		ModelAndView mv = new ModelAndView("mainBoard/mainBoard");
		
		int limit = 10;
		
		
		
		scvo.setBbs_id("NOTI");
		List<MainBoardVO> list1 = mainBoardService.getBbsList(scvo, limit);
		scvo.setBbs_id("INQU");
		List<MainBoardVO> list2 = mainBoardService.getBbsList(scvo, limit);
		scvo.setBbs_id("CONS");
		List<MainBoardVO> list3 = mainBoardService.getBbsList(scvo, limit);
		scvo.setBbs_id("FREE");	
		List<MainBoardVO> list4 = mainBoardService.getBbsList(scvo, limit);

		mv.addObject("list1", list1);
		mv.addObject("list2", list2);
		mv.addObject("list3", list3);
		mv.addObject("list4", list4);

		return mv;
	}

	@RequestMapping("/boardListAll")
	public ModelAndView getBoardList(@RequestParam("bbs_id") String bbs_id, SearchVO sc) {
		ModelAndView mv = new ModelAndView("mainBoard/boardListAll");
		String bbsName = mainBoardService.getBbsName(bbs_id);
		
		sc.setBbs_id(bbs_id);
		
		//페이지 용 전체 게시물 갯수 구하기(활성화, 댓글 x , 답글포함)
		int totalCnt = mainBoardService.getCountWrList(sc);
		//혹시모르니 totalcnt 넣어두기(검색 총 몇 건 용..?)
		mv.addObject("totalCnt", totalCnt);
		
		MainBoardPagingVO pgvo = new MainBoardPagingVO(totalCnt,sc);
		List<MainBoardVO> list = mainBoardService.getBbsList(sc, null);
		System.out.println("sc 페이지사이즈 : "+sc.getPageSize());
		System.out.println("sc 옵셋 : "+sc.getOffset());
		System.out.println("sc 키워드 : "+sc.getKeyword());
			
		
		mv.addObject("pg", pgvo);
		mv.addObject("sc", sc);
		mv.addObject("list", list);
		mv.addObject("bbsName", bbsName);
		mv.addObject("bbs_id", bbs_id);
		System.out.println("boardListAll bbsid 확인" + bbs_id);

		return mv;
	}

	@RequestMapping("/boardOneList")
	public ModelAndView boardOneList(HttpServletRequest request,HttpSession session) {
		String wr_id = request.getParameter("wr_id"); //댓글기준에서는 parentid
		String bbs_id = request.getParameter("bbs_id");

		ModelAndView mv = new ModelAndView("mainBoard/boardOneList");
		
		//조회수 증가시키기
		 mainBoardService.updateViewNum(wr_id);
		

		System.out.println("wrid,bbsid 확인(boardOneList) :" + wr_id + "," + bbs_id);
		
		try {
			//세션 vo 사용하기 ( 글삭제,수정버튼 등 용도)
			SessionUserVO suvo = (SessionUserVO) session.getAttribute("sessionUser");
			String sessionUserId = (suvo != null) ? suvo.getUser_id() : null;
			
			String mber_nm = null;
			 if (sessionUserId != null) {
		            // 세션 ID를 이용해 사용자 정보를 가져옴 (댓글용 닉네임 등)
					//세션아이디로 세션 닉네임 주기 ( 댓글용)
		            MemberVO mbvo = mainBoardService.getUserInfo(sessionUserId);
		    		/* String password = mbvo.getPassword(); */
		        	mber_nm = mbvo.getMber_nm();
		        } else {
		            System.out.println("로그인 정보가 없습니다.");
		        }
			
	
	
		
		 	// 상세보기
				MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);
				//댓글도 불러오기
				List<MainBoardVO> list = mainBoardService.getCommList(wr_id); //원글 wr_id를  parent_id로 갖는 '댓글' 검색.
				
				mv.addObject("list", list);

				mv.addObject("mvo", mvo);
				mv.addObject("sessionUserId", sessionUserId);
				mv.addObject("mber_nm", mber_nm);

				return mv;
		} catch (Exception e) {
			System.out.println(e);
		}
		return new ModelAndView("mainBoard/boardError");
		
	}

	@RequestMapping("/boardOneListWrite")
	public ModelAndView boardOneListWrite(@RequestParam("bbs_id") String bbs_id,HttpSession session) {
		ModelAndView mv = new ModelAndView("mainBoard/writePage");
		
		//세션 vo 사용하기 ( 글삭제,수정버튼 등 용도)
		SessionUserVO suvo = (SessionUserVO) session.getAttribute("sessionUser");
		String sessionUserId = (suvo != null) ? suvo.getUser_id() : null;
		
		
		mv.addObject("bbs_id", bbs_id);
		mv.addObject("sessionUserId", sessionUserId);
		
		if (sessionUserId == null) {
			return new ModelAndView("login&join/login");
		}
		
		System.out.println("/boardOneListWrite에서 bbs_id 전달됨: " + bbs_id);
		return mv;
		
		
	
		
	}

	@RequestMapping("/boardOneListWriteOK")
	public ModelAndView boardOneListWriteOK(MainBoardVO mvo, HttpServletRequest request) {
		String bbs_id = request.getParameter("bbs_id");
		String mber_id = request.getParameter("mber_id");
		String parent_id = request.getParameter("parent_id");
		String sort_ordr = request.getParameter("sort_ordr");
		//병원이름 검색한거로 병원 id 검색하기
		 String h_name = request.getParameter("h_name");
		    Integer h_id = null;

		    
		    
		
		    try {
		        h_id = mainBoardService.getH_Id(h_name);
		    } catch (Exception e) {
		        System.out.println("Error fetching h_id for h_name: " + e.getMessage());
		    }
		    
		    // h_id가 null이거나 0일 경우 기본값 설정
		    mvo.setH_id(h_id != null && h_id != 0 ? h_id : 0); 
		
		try {
			ModelAndView mv = new ModelAndView("redirect:/boardListAll");
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			

			
			
			
			
			MultipartFile file = mvo.getFile_name();
			if (file.isEmpty()) {
				mvo.setAtch_file_id("");
			} else {
				UUID uuid = UUID.randomUUID();
				String atch_file_id = uuid.toString() + "_" + file.getOriginalFilename();
				mvo.setAtch_file_id(atch_file_id);

				// 업로드
				file.transferTo(new File(path,atch_file_id));
			}

			System.out.println("writeOK전용체크"+bbs_id+parent_id+sort_ordr);
			mv.addObject("bbs_id", bbs_id);  //리다이렉트용
			mv.addObject("parent_id", parent_id); //리다이렉트용
			
			//mber_id로  패스워드, 비번 따내기.
			MemberVO mbvo = mainBoardService.getUserInfo(mber_id);
			
			String password = mbvo.getPassword();
			String mber_nm = mbvo.getMber_nm();
			
			mvo.setMber_nm(mber_nm);
			mvo.setPassword(password);
			
		
			
			int result = mainBoardService.setBoardVO(mvo);
			if (result > 0) {
			

				return mv;
			}

			return new ModelAndView("mainboard/boardError");
		} catch (Exception e) {
			System.out.println(e);
			return new ModelAndView("mainboard/boardError");
		}
	}

	// 이미지저장 등
	@RequestMapping(value = "/boardSaveImg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> saveImg(@RequestParam("s_file") MultipartFile file, HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String fname = null;
			if (file.getSize() > 0) {
				String path = request.getSession().getServletContext().getRealPath("/resources/upload");
				UUID uuid = UUID.randomUUID();
				fname = uuid.toString() + "_" + file.getOriginalFilename();
				// 업로드
				file.transferTo(new File(path, fname));
			}

			map.put("path", "resources/upload");
			map.put("fname", fname);
			return map;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	@GetMapping("/boardDownloadFile")
	public void fileDown(HttpServletRequest request, HttpServletResponse response) {
		try {
			String f_name = request.getParameter("atch_file_id");
			System.out.println(f_name);
			String path = request.getSession().getServletContext().getRealPath("/resources/upload/" + f_name);
			String r_path = URLEncoder.encode(path, "UTF-8");

			String tname = f_name.substring(f_name.indexOf("_") + 1, f_name.length());
			// 브라우저 설정
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename=" + tname);

			// 실제 가져오기
			File file = new File(new String(path.getBytes(), "UTF-8"));
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();

			FileCopyUtils.copy(in, out);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 업데이트페이지 이동
	@RequestMapping("/boardOneListUpdate")
	public ModelAndView boardOneListUpdate(@ModelAttribute("wr_id") String wr_id,
			@ModelAttribute("bbs_id") String bbs_id) {
		ModelAndView mv = new ModelAndView("mainBoard/updatePage");
		// DB 에서 b_idx를 이용해서 정보 가져오기. 전에 만들었던 getWr 그대로 사용. 실제 수정은 UpdateOK에서 진행
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);
		if (mvo != null) {
			mv.addObject("mvo", mvo);
			System.out.println("wrid 확인 : " + wr_id);
			System.out.println("bbsid 확인 :" + bbs_id);
			return mv;
		}

		return null;
	}

	// 실제 업데이트 하는 곳
	@PostMapping("/boardOneListUpdateOK")
	public ModelAndView boardOneListUpdateOK(MainBoardVO mvo, HttpServletRequest request) {
		String bbs_id = request.getParameter("bbs_id");
		String wr_id = request.getParameter("wr_id");
		ModelAndView mv = new ModelAndView("redirect:/boardOneList");

		
		
		
		try {
			String path = request.getSession().getServletContext().getRealPath("/resources/upload");
			MultipartFile file = mvo.getFile_name();
			String old_atch_file = mvo.getOld_atch_file_id();

			if (file.isEmpty()) {
				mvo.setAtch_file_id(old_atch_file);
			} else {
				UUID uuid = UUID.randomUUID();
				String atch_file_id = uuid.toString() + "_" + file.getOriginalFilename();
				mvo.setAtch_file_id(atch_file_id);

				// 실제업로드
				file.transferTo(new File(path, atch_file_id));
			}
			// 리다이렉트용 게시판id, 글id 주기
			mv.addObject("bbs_id", bbs_id);
			mv.addObject("wr_id", wr_id);

			int result = mainBoardService.updateBoardVO(mvo); 
			if (result > 0) {
				return mv;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		/*
		 * } else { // 비밀번호가 틀리다. mv.setViewName("bbs/update"); mv.addObject("password chk",
		 * "fail"); mv.addObject("bvo", bvo); return mv; }
		 */

		return new ModelAndView("mainBoard/error");

	}

	// 삭제페이지 이동
	@RequestMapping("/boardOneListDelete")
	public ModelAndView boardOneListDelete(HttpServletRequest request) {

		String wr_id = request.getParameter("wr_id");
		String bbs_id = request.getParameter("bbs_id");

		ModelAndView mv = new ModelAndView("mainBoard/deletePage");
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);
		mv.addObject("mvo", mvo);

		return mv;
	}

	@RequestMapping("/boardOneListDeleteOk")
    public ModelAndView boardOneListDeleteOk(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String wr_id = request.getParameter("wr_id");
        String bbs_id = request.getParameter("bbs_id");
        // 삭제창에서 입력한 비밀번호값 받아오기.
        String passwordInput = request.getParameter("passwordInput");
        // 글제목, 게시판제목으로 글 검색
        MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);
        // 게시물의 사용자 패스워드 DB에서 뽑아내옴
        System.out.println(wr_id);
        System.out.println(bbs_id);
        String password  = mvo.getPassword();
        System.out.println(password);
        // 비밀번호 체크
        if (passwordEncoder.matches(passwordInput,password )) {
            // 맞으면 삭제 진행.
            int result = mainBoardService.deleteBoardVO(mvo);
            if (result > 0) {
                mv.setViewName("redirect:/boardListAll");
                mv.addObject("bbs_id",bbs_id);
                return mv;
            }
        } else {
            // 비밀번호가 틀리다.
            mv.setViewName("mainBoard/deletePage");
            mv.addObject("passwordchk", "fail");
            mv.addObject("mvo", mvo); //재시도할떄도 password  비교해야함
            return mv;
        }

        return new ModelAndView("mainBoard/boardError");
    }

	
	@RequestMapping("/comment_insert")
	public ModelAndView comment_insert(MainBoardVO mvo,HttpServletRequest request, HttpSession session) {
		//리다이렉트할 페이지 우선 정함.
		ModelAndView mv = new ModelAndView("redirect:/boardOneList");
		String parent_id = request.getParameter("parent_id"); //부모글의 wr_id 리다이렉트용. 여기서 wr_id= 부모글 id.
		String bbs_id = request.getParameter("bbs_id"); //부모글의 게시판. bbs_id 리다이렉트용
		//세션아이디로 계정의 비밀번호, 유저네임 필요함.
		String mber_id = request.getParameter("mber_id");
	
		
		
		//멤버에서 아이디로 유저네임, 비밀번호 검색
		try {
			MemberVO mbvo = mainBoardService.getUserInfo(mber_id);
			
			String password = mbvo.getPassword();
			String mber_nm = mbvo.getMber_nm();
			
			System.out.println("commentinsert에서 redirect boardonelist 확인용 "+parent_id+bbs_id); //리다이렉트 확인용
			
			//mvo에  setComment를 위해서 넣어야함 
			mvo.setMber_nm(mber_nm);
			mvo.setPassword(password);
			
			int result = mainBoardService.setComment(mvo); // 댓글넣기. 공통 명령어씀 
			mv.addObject("bbs_id", bbs_id);		//리다이렉트용
			mv.addObject("wr_id", parent_id);	//주의-리다이렉트용. parent_id는 원글의 wr_id다.
		
	            return mv;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("login&join/login");
	
		
		
		
	
	}
	
	@RequestMapping("/comment_delete")
	public ModelAndView comment_delete(HttpServletRequest request) {

		String wr_id = request.getParameter("wr_id");
		String bbs_id = request.getParameter("bbs_id");
		String h_id = request.getParameter("h_id");


		ModelAndView mv = new ModelAndView("mainBoard/commentDeletePage");
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);

		mv.addObject("mvo", mvo);
		mv.addObject("h_id", h_id);

		return mv;
	}
	@RequestMapping("/comment_deleteOk")
	public ModelAndView comment_deleteOk(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		//리뷰 구분 ( 리뷰는  h_id가 있음)
		String parent_id = request.getParameter("parent_id"); //댓글의 원게시물 게시물아이디
		String wr_id = request.getParameter("wr_id"); //댓글 게시물아이디
		String bbs_id = request.getParameter("bbs_id"); // 댓글게시판아이디
		// 삭제창에서 입력한 비밀번호값 받아오기.
		String passwordInput = request.getParameter("passwordInput");
		// 글제목, 게시판제목으로 글관련 mvo 검색
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id); //댓글삭제시 댓글비교용으로 db 갓다옴.
		// 게시물의 사용자 패스워드 DB에서 뽑아내옴
		System.out.println("댓글삭제용"+wr_id);
		System.out.println("댓글삭제용"+bbs_id);
		String password  = mvo.getPassword(); //댓글비교용 db의 pw. 
		// 비밀번호 체크
		if (passwordEncoder.matches(passwordInput,password )) {
			// 맞으면 삭제 진행.
			int result = mainBoardService.deleteBoardVO(mvo);
			if (result > 0) {
					mv.setViewName("redirect:/boardOneList");
					mv.addObject("bbs_id",bbs_id);
					mv.addObject("wr_id",parent_id);  //삭제되었으면 원 게시물로 이동해야함.
					return mv;
				}
			
			}
		 else {
			// 비밀번호가 틀리다.
			System.out.println("댓글삭제실패용parent_id"+parent_id);
			System.out.println("댓글삭제실패용wr_id"+wr_id);
			System.out.println("댓글삭제실패용bbs_id"+bbs_id);
		
			
				mv.setViewName("mainBoard/commentDeletePage");
				mv.addObject("passwordchk", "fail");
				mv.addObject("mvo", mvo); //재시도할떄도 password  비교해야함
				return mv;
			
			
		}

		return new ModelAndView("mainBoard/boardError");
	}
	
	@RequestMapping("/boardOneListWrite2")
	public ModelAndView boardOneListWrite2(HttpServletRequest request,HttpSession session) {
		String parent_id = request.getParameter("parent_id");
		String bbs_id = request.getParameter("bbs_id");
		//페이지불러오기 
		ModelAndView mv = new ModelAndView("mainBoard/writePage2");
		
		//세션에서 아이디검색하고, 저장해야함
		//세션 vo 사용하기 ( 글삭제,수정버튼 등 용도)
		SessionUserVO suvo = (SessionUserVO) session.getAttribute("sessionUser");
		String sessionUserId = (suvo != null) ? suvo.getUser_id() : null;
		
		// 세션 아이디가 없으면 로그인 페이지로 리다이렉트
	    if (sessionUserId == null) {
	        return new ModelAndView("login&join/login");  // 로그인 페이지 URL에 맞게 수정
	    }
		
		
		//세션아이디로 세션 닉네임 주기 ( 댓글용)
		MemberVO mbvo;
		try {
			mbvo = mainBoardService.getUserInfo(sessionUserId);
			/* String password = mbvo.getPassword(); */
			String mber_nm = mbvo.getMber_nm();
			
			
			//parent_id를 갖는 글 검색해서 가장높은 sortordr 검색하기(나중에추가하기)
			Integer maxSort_ordr= mainBoardService.getChildSelect(parent_id); //null이 될수도 있기때문에 Integer 쓴다 
			int sort_ordr = (maxSort_ordr == null ? 1 : maxSort_ordr + 1);

			
			
			
			//부모글 bbs id wrid  주기 ... 
			mv.addObject("sessionUserId", sessionUserId);
			mv.addObject("bbs_id", bbs_id);
			mv.addObject("parent_id", parent_id);
			mv.addObject("sort_ordr", sort_ordr);
			System.out.println("write2전용maxSort_ordr"+maxSort_ordr);
			System.out.println("write2전용bbsid"+bbs_id);
			System.out.println("write2전용parent_id"+parent_id);
			System.out.println("write2전용sort_ordr"+sort_ordr);
			
			
			//기존 답글들 1씩  update 하기 
			
			
			
			
			
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return new ModelAndView("mainBoard/boardError");
	}
	//상담사례( hospital이긴 하나 mainboard db라서  여기서하기로 함.  
	
	@RequestMapping("/hospitalConsult")
	public ModelAndView hospitalConsult(Model model, HttpSession session) {
	    ModelAndView mv = new ModelAndView("hospitalClick/hospitalConsult");
	    HospitalVO hvo = (HospitalVO) session.getAttribute("hvo");

	        String h_id = hvo.getH_id();
	        

	        // 병원아이디로 게시물 리스트 검색
	        int totalCount = mainBoardService.getCheckConsultReple(h_id);
	        	System.out.println("토탈카운트"+totalCount);
	        if (totalCount != 0) {
	            List<MainBoardVO> list = mainBoardService.getHospitalConsult(h_id);
	            System.out.println("시스아웃 리스트 " + list);
	            LocalDate now = LocalDate.now();  // 현재 날짜를 LocalDate로 설정
	            long daysDifference;
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	            for (MainBoardVO k : list) {
	                // wr_id를 parent id로 갖는 게시물이 있는지 검색
	                int childCount = mainBoardService.getCountHospitalConsultReple(k.getWr_id());
	                k.setChildCount(childCount);

	                // start_dt를 LocalDate로 변환
	                LocalDate startDate = LocalDate.parse(k.getStart_dt(), formatter);

	                // start_dt와 현재 날짜(now)의 일자 차이를 계산
	                daysDifference = ChronoUnit.DAYS.between(startDate, now);
	                k.setDaysDifference(daysDifference);
	            }

	            mv.addObject("list", list);
	            return mv;
	        }
	    

	    return mv;
	}
	//상담사례디테일
			@RequestMapping("/hospitalConsultDetail")
			public ModelAndView hospitalConsultDetail(Model model, HttpSession session,String wr_id) {
			    ModelAndView mv = new ModelAndView("hospitalClick/hospitalConsultDetail");
			    HospitalVO hvo = (HospitalVO) session.getAttribute("hvo");
			    
		        String h_id = hvo.getH_id();
		        //상담사례가 있는경우에만 이 탭이 보이므로  예외처리 생략.
		        //우선 hid를 통한 글 전체 다 가져옴. (답글, 원글포함) 원글 제외 필요함. 상담사례니까 게시판아이디 CONS 고정
		        MainBoardVO mvo = mainBoardService.getWrList(wr_id, "CONS");
		        
		        mv.addObject("mvo", mvo);
			    
			    return mv;
			}
			
			
	//후기 
	@RequestMapping("/hospitalReview")
	public ModelAndView hospitalReview(Model model, HttpSession session,String wr_id) {
		ModelAndView mv = new ModelAndView("hospitalClick/hospitalReview");
		
		HospitalVO hvo = (HospitalVO) session.getAttribute("hvo");
		
		String h_id = hvo.getH_id();
			
		  // 병원아이디로 게시물(리뷰) 리스트 검색
        	 try {
        		 List<MainBoardVO> list = mainBoardService.getReview(h_id);
  	            LocalDate now = LocalDate.now();  // 현재 날짜를 LocalDate로 설정
  	            long daysDifference;
  	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  	            for (MainBoardVO k : list) {

  	                // start_dt를 LocalDate로 변환
  	                LocalDate startDate = LocalDate.parse(k.getStart_dt(), formatter);

  	                // start_dt와 현재 날짜(now)의 일자 차이를 계산
  	                daysDifference = ChronoUnit.DAYS.between(startDate, now);
  	                k.setDaysDifference(daysDifference);
  	            }
  	            
  	          //세션 vo 사용하기 ( 글삭제,수정버튼 등 용도) 
  	  	  		SessionUserVO suvo = (SessionUserVO) session.getAttribute("sessionUser");
  	  	  		String sessionUserId = (suvo != null) ? suvo.getUser_id() : null;
  	  	  		//유저닉네임 준비 
  	  	  		MemberVO mbvo = mainBoardService.getUserInfo(sessionUserId);
  	  	  		String h_name = hvo.getH_name();
  	  	  		
  	  	  		mv.addObject("list", list);  //병원에 대한 리뷰리스트.  조회용 리스트는 var k써야함~~ 
  	  	  		mv.addObject("hvo", hvo); // 병원아이디 . 리뷰조회용, 리뷰 쓸떄용
  	  	  		mv.addObject("mbvo", mbvo); //멤버정보. 리뷰 쓸떄 용
				/*
				 * if (sessionUserId == null) { return new ModelAndView("login&join/login"); }
				 */
  	  	  	
  	        
  	            return mv;
 
			} catch (Exception e) {
				// TODO: handle exception
			}
 	           
	
		return null; 
	}
	@RequestMapping("/reviewWrite")
	public ModelAndView reviewWrite(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("redirect:/hospitalDetail");
		String bbs_id = request.getParameter("bbs_id");
		int h_id = Integer.valueOf(request.getParameter("h_id"));
		String sort_ordr = request.getParameter("sort_ordr");
		String mber_id = request.getParameter("mber_id");
		String mber_nm = request.getParameter("mber_nm");
		String password = request.getParameter("password");
		String wr_content = request.getParameter("wr_content");
		String h_score = request.getParameter("h_score");
		
		MainBoardVO mvo = new MainBoardVO();
		mvo.setBbs_id(bbs_id);
		mvo.setH_id(h_id);
		mvo.setWr_content(wr_content);
		mvo.setSort_ordr(sort_ordr);
		mvo.setMber_id(mber_id);
		mvo.setMber_nm(mber_nm);
		mvo.setPassword(password);
		mvo.setH_score(h_score);
		
		mainBoardService.setBoardVO(mvo);
		
		
		
		mv.addObject("h_id", h_id);
		
		return mv;
		
	}
	

	@RequestMapping("/reviewDelete")
	public ModelAndView reviewDelete(HttpServletRequest request) {

		String wr_id = request.getParameter("wr_id");
		String bbs_id = request.getParameter("bbs_id");
		String h_id = request.getParameter("h_id");


		ModelAndView mv = new ModelAndView("mainBoard/reviewDeletePage");
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);

		mv.addObject("mvo", mvo);
		mv.addObject("h_id", h_id);

		return mv;
	}
	@RequestMapping("/reviewDeleteOk")
	public ModelAndView reviewDeleteOk(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		//리뷰 구분 ( 리뷰는  h_id가 있음)
		String h_id = request.getParameter("h_id");
		String parent_id = request.getParameter("parent_id"); //댓글의 원게시물 게시물아이디
		String wr_id = request.getParameter("wr_id"); //댓글 게시물아이디
		String bbs_id = request.getParameter("bbs_id"); // 댓글게시판아이디
		// 삭제창에서 입력한 비밀번호값 받아오기.
		String passwordInput = request.getParameter("passwordInput");
		// 글제목, 게시판제목으로 글관련 mvo 검색
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id); //댓글삭제시 댓글비교용으로 db 갓다옴.
		// 게시물의 사용자 패스워드 DB에서 뽑아내옴
		System.out.println("댓글삭제용"+wr_id);
		System.out.println("댓글삭제용"+bbs_id);
		String password  = mvo.getPassword(); //댓글비교용 db의 pw. 
		// 비밀번호 체크
		if (passwordEncoder.matches(passwordInput,password )) {
			// 맞으면 삭제 진행.
			int result = mainBoardService.deleteBoardVO(mvo);
			if (result > 0) {
					mv.setViewName("redirect:/hospitalDetail?h_id="+h_id);
					mv.addObject("bbs_id",bbs_id);
					mv.addObject("wr_id",parent_id);  //삭제되었으면 원 게시물로 이동해야함.
					return mv;
				}
			
			}
		 else {
			// 비밀번호가 틀리다.
			System.out.println("리뷰삭제실패용parent_id"+parent_id);
			System.out.println("리뷰삭제실패용wr_id"+wr_id);
			System.out.println("리뷰삭제실패용bbs_id"+bbs_id);
		
			
				mv.setViewName("mainBoard/reviewDeletePage");
				mv.addObject("passwordchk", "fail");
				mv.addObject("mvo", mvo); //재시도할떄도 password  비교해야함
				mv.addObject("h_id", h_id); //재시도할떄도 h_id 있어야함
				return mv;
			
			
		}

		return new ModelAndView("mainBoard/boardError");
	}

	
	@RequestMapping("/boardOneListIssue")
	public ModelAndView boardOneListIssue(HttpServletRequest request) {

		String wr_id = request.getParameter("wr_id");
		String bbs_id = request.getParameter("bbs_id");
		String sessionUserId = request.getParameter("sessionUserId");


		ModelAndView mv = new ModelAndView("mainBoard/boardOneListIssue");
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);

		mv.addObject("mvo", mvo);
		mv.addObject("sessionUserId", sessionUserId);
		

		return mv;
	}
	@RequestMapping("/boardOneListIssueOk")
	public ModelAndView boardOneListIssueOk(HttpServletRequest request) {

		String wr_id = request.getParameter("wr_id");
		String bbs_id = request.getParameter("bbs_id");
		//�����Ͱ� �״�� ���������� ���Ǹ����ؼ�...
		String issue_people = request.getParameter("sessionUserId");
		String issue_option = request.getParameter("issue_option");
		String issue_post = request.getParameter("issue_post");


		ModelAndView mv = new ModelAndView("redirect:/boardOneList");
		
		//신고된 게시물 알아오기 
		MainBoardVO mvo = mainBoardService.getWrList(wr_id, bbs_id);
		//신고된 게시물에 정보 추가하기 
		mvo.setIssue_people(issue_people);
		mvo.setIssue_option(issue_option);
		mvo.setIssue_post(issue_post);
		
		int result = mainBoardService.updateBoardVO(mvo); 
		if (result > 0) {
			mv.addObject("bbs_id",bbs_id);
			mv.addObject("wr_id",wr_id);
			return mv;
		}
		mv.addObject("bbs_id",bbs_id);
		mv.addObject("wr_id",wr_id);
		

		
		return mv;
	}
	

	
}