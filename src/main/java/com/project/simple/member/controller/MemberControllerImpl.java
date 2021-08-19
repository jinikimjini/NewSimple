package com.project.simple.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.project.simple.member.service.MemberService;
import com.project.simple.member.vo.MemberVO;
import com.project.simple.page.Criteria;
import com.project.simple.page.PageMaker;

/**
 * Handles requests for the application home page.
 */
@Controller("memberController")
public class MemberControllerImpl implements MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;
	@Autowired
	BCryptPasswordEncoder pwdEncoder;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		return "main";
	}

	// ����α����۾� ppt226
	// @Override
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		ModelAndView mav = new ModelAndView();
		MemberVO memberVO = memberService.login(member);
		boolean pwdMatch = pwdEncoder.matches(member.getmemPwd(), memberVO.getmemPwd());

		if (memberVO != null && pwdMatch == true) {
			HttpSession session = request.getSession();
			String admin = memberVO.getlogintype();
			if (admin.equals("������")) {
				session.setAttribute("admin", memberVO);
				session.setAttribute("AdminisLogOn", true);
			} else {
				session.setAttribute("member", memberVO);
				session.setAttribute("isLogOn", true);
			}

			mav.setViewName("redirect:/main.do");
		} else {
			out.println("<script>");
			out.println("alert('���̵� �Ǵ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();

		}
		return mav;
	}

	// ���̵� ã��
	@RequestMapping(value = "/findId.do", method = RequestMethod.POST)
	public ModelAndView findId(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		ModelAndView mav = new ModelAndView();
		PrintWriter out = response.getWriter();
		memberVO = memberService.findId(member);
		if (memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("findId", true);
			mav.setViewName("redirect:/login_04.do");

		} else {
			out.println("<script>");
			out.println("alert('ã���ô� ���̵� �����ϴ�.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return null;
		}
		return mav;
	}

	// ���̵� ã�� �Ϸ�
	@RequestMapping(value = "/login_04.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}

	// �α׾ƿ� �۾�
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		if (session.getAttribute("member") != null) {
			session.removeAttribute("member");
			session.removeAttribute("isLogOn");
		}
		if (session.getAttribute("admin") != null) {
			session.removeAttribute("admin");
			session.removeAttribute("AdminisLogOn");

		}
		if (session.getAttribute("myInfo") != null) {
			session.removeAttribute("myInfo");

		}

		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/main.do");

		return mav;
	}

	// ȸ�������۾�
	@Override
	@RequestMapping(value = "/addMembers.do", method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String pwd = member.getmemPwd();
		String memPwd = pwdEncoder.encode(pwd);
		member.setmemPwd(memPwd);
		int result = 0;
		result = memberService.addMember(member);
		ModelAndView mav = new ModelAndView("redirect:/join_02.do");
		return mav;
	}

	// �׾Ʒ� DB�� �߰������۾� �� �α����۾�
	@Override
	@RequestMapping(value = "/addMembers_naver.do", method = RequestMethod.POST)
	public ModelAndView addMember_naver(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		request.setCharacterEncoding("utf-8");
		int result = 0;
		result = memberService.addMember_naver(member);

		// �α����۾�
		memberVO = memberService.login_naver(member);

		if (memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			mav.setViewName("redirect:/join_02.do");
		} else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/login_01.do");
		}
		return mav;
	}

	// ���̹��α��ν� DB�� ���� ������ �߰����� ��ġ�� �ʰ� �ٷ� �α���
	@RequestMapping(value = "/login_naver.do", method = RequestMethod.GET)
	public ModelAndView naver_login(RedirectAttributes rAttr, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		session.setAttribute("member", memberVO);
		session.setAttribute("isLogOn", true);
		mav.setViewName("redirect:/main.do");

		return mav;
	}

	// ȸ��Ż���۾�(�Ϲ�ȸ��)
	@RequestMapping(value = "/removeMember.do", method = RequestMethod.POST)
	public ModelAndView removeMember(@ModelAttribute("removemember") MemberVO removemember, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes rAttr) throws Exception {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");
		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");
		String loginType = member.getlogintype();
		String memId = member.getmemId();
		String inputId = removemember.getmemId();
		boolean pwdMatch = pwdEncoder.matches(removemember.getmemPwd(), member.getmemPwd());

		if (pwdMatch != true || !memId.equals(inputId)) {
			rAttr.addAttribute("result", false);
			mav.setViewName("redirect:/deletemember.do");

		} else if (pwdMatch == true && memId.equals(inputId)) {
			String memPwd = member.getmemPwd();
			removemember.setmemPwd(memPwd);
			int result = memberService.removeMember(removemember);
			session.removeAttribute("member");
			session.removeAttribute("isLogOn");
			mav.setViewName("redirect:/drop_out.do");
		}
		return mav;
	}

	// ȸ��Ż���۾�(SNSȸ��)
	@RequestMapping(value = "/removeMember_SNSmember.do", method = RequestMethod.POST)
	public ModelAndView removeMember_SNSmember(@ModelAttribute("removemember") MemberVO removemember,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes rAttr) throws Exception {

		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");
		String loginType = member.getlogintype();
		String sns = removemember.getSNS();
		String memId = member.getmemId();
		String memPwd = member.getmemPwd();
		if (!sns.equals("���ݻ���")) {
			rAttr.addAttribute("result", false);
			mav.setViewName("redirect:/deletemember.do");
		} else if (sns.equals("���ݻ���") && !loginType.equals("�Ϲ�")) {
			removemember.setmemId(memId);
			removemember.setmemPwd(memPwd);
			int result = memberService.removeMember(removemember);
			session.removeAttribute("member");
			session.removeAttribute("isLogOn");
			mav.setViewName("redirect:/drop_out.do");
		}
		return mav;

	}

	// ȸ����������
	@RequestMapping(value = "/modMember.do", method = RequestMethod.POST)
	public ModelAndView modMember(@ModelAttribute("modmember") MemberVO modmember, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes rAttr) throws Exception {
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		request.setCharacterEncoding("utf-8");
		String pwd = modmember.getmemPwd();

		if (pwd != null) {
			String memPwd = pwdEncoder.encode(pwd);
			modmember.setmemPwd(memPwd);
			int result = 0;
			result = memberService.modMember(modmember);
			session.removeAttribute("member");
			session.removeAttribute("isLogOn");
			mav.setViewName("redirect:/mypage_10.do");
		} else if (pwd == null) { //SNS�� �α����� ȸ���� ��й�ȣ�� null�� �̹Ƿ� pwd==null �̴�.
			int result = 0;
			result = memberService.modMember(modmember);
			session.removeAttribute("member");
			session.removeAttribute("isLogOn");
			mav.setViewName("redirect:/mypage_10.do");
		}
		return mav;
	}

	// �Ϲ�ȸ������ ��й�ȣȮ��
	@RequestMapping(value = "/modPwdCheck01.do", method = RequestMethod.POST)
	public ModelAndView mypage_03(@ModelAttribute("confirmPwd") MemberVO confirmPwd, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes rAttr) throws Exception {
		ModelAndView mav = new ModelAndView();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");

		boolean pwdMatch = pwdEncoder.matches(confirmPwd.getmemPwd(), member.getmemPwd());

		// String sessionmemPwd = member.getmemPwd();
		// String memPwd = confirmPwd.getmemPwd();

		if (pwdMatch != true) {
			rAttr.addAttribute("result", false);
			out.println("<script>");
			out.println("alert('��й�ȣ�� ��ġ���� �ʽ��ϴ�.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			mav.setViewName("redirect:/mypage_02.do");
		} else {
			mav.setViewName("redirect:/mypage_03.do");
		}
		return mav;
	}

	@RequestMapping(value = "/mypage_03.do", method = RequestMethod.GET)
	private ModelAndView mypage_03(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/drop_out.do", method = RequestMethod.GET)
	private ModelAndView drop_out(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/deletemember.do", method = RequestMethod.GET)
	private ModelAndView deletemember(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/mypage_02.do", method = RequestMethod.GET)
	private ModelAndView mypage_02(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO vo = (MemberVO) session.getAttribute("member");
		String loginType = vo.getlogintype();

		if (loginType.equals("�Ϲ�")) {
			mav.addObject("loginType", loginType);
			mav.setViewName(viewName);
		} else {

			mav.setViewName("mypage_04");

		}

		return mav;
	}

	@RequestMapping(value = "/mypage_10.do", method = RequestMethod.GET)
	private ModelAndView mypage_10(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/join_02.do", method = RequestMethod.GET)
	private ModelAndView join_02(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/join_01.do", method = RequestMethod.GET)
	private ModelAndView join_01(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/join_03.do", method = RequestMethod.GET)
	private ModelAndView join_03(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/join_04.do", method = RequestMethod.GET)
	private ModelAndView join_04(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@RequestMapping(value = "/storeinfomation.do", method = RequestMethod.GET)
	private ModelAndView storeinfomation(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	// ���̵�,��й�ȣ ã��
	@RequestMapping(value = "/login_03.do", method = RequestMethod.GET)
	private ModelAndView login_03(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	// �� ��й�ȣ ����
	@RequestMapping(value = "/login_05.do", method = RequestMethod.GET)
	private ModelAndView login_05(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	// �� ��й�ȣ ���� �Ϸ�
	@RequestMapping(value = "/login_06.do", method = RequestMethod.GET)
	private ModelAndView login_06(HttpServletRequest request, HttpServletResponse response) {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();
		session.removeAttribute("memberPwd");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	// ������ ȸ������Ʈ
	@Override
	@RequestMapping(value = "/admin_listmember.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listMembers(Criteria cri, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		List<MemberVO> membersList = memberService.listMembers(cri);
		int memberCount = memberService.memberCount();
		ModelAndView mav = new ModelAndView(viewName);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(memberCount);
		int pageNum = pageMaker.getCri().getPage();

		mav.addObject("pageNum", pageNum);
		mav.addObject("membersList", membersList);
		mav.addObject("pageMaker", pageMaker);

		return mav;
	}

	// ������ ȸ�� �˻� ����Ʈ
	@Override
	@RequestMapping(value = "/admin_listmember/memberSearch.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView memberSearch(@RequestParam("search") String search,
			@RequestParam("searchType") String searchType, Criteria cri, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);

		Map<String, Object> memberSearchMap = new HashMap<String, Object>();
		int pageStart = cri.getPageStart();
		int perPageNum = cri.getPerPageNum();
		memberSearchMap.put("pageStart", pageStart);
		memberSearchMap.put("perPageNum", perPageNum);
		memberSearchMap.put("search", search);
		System.out.println(search);
		memberSearchMap.put("searchType", searchType);
		System.out.println(searchType);
		memberSearchMap = memberService.memberSearch(memberSearchMap);
		System.out.println(memberSearchMap);
		int memberSearchCount = memberService.memberSearchCount(memberSearchMap);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		int pageNum = pageMaker.getCri().getPage();
		memberSearchMap.put("pageNum", pageNum);
		pageMaker.setTotalCount(memberSearchCount);
		mav.addObject("memberSearchMap", memberSearchMap);
		mav.addObject("pageMaker", pageMaker);
		mav.addObject("pageNum", pageNum);

		return mav;

	}

	// ��й�ȣ ã��
	@RequestMapping(value = "/find_pw.do", method = RequestMethod.POST)
	public void find_pw(@ModelAttribute MemberVO memberVO, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		memberService.find_pw(request, response, memberVO);
	}

	// ���̵� �ߺ� Ȯ��
	@Override
	@RequestMapping(value = "/overlapped.do", method = RequestMethod.POST)
	public ResponseEntity overlapped(@RequestParam("memId") String memId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResponseEntity resEntity = null;
		String result = memberService.overlapped(memId);
		resEntity = new ResponseEntity(result, HttpStatus.OK);
		return resEntity;

	}

	@RequestMapping(value = "/check/sendSMS", method = RequestMethod.POST)
	public @ResponseBody void sendSMS(String memPhoneNum, HttpServletRequest request, HttpServletResponse response,
			MemberVO memberVO) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// ���̵� ������
		if (memberService.check_id(memberVO.getmemId()) == 0) {
			out.print("���̵� �����ϴ�.");
			out.close();
		} // ���Կ� ����� �̸����� �ƴϸ�
		else if (!memberVO.getmemPhoneNum().equals(memberService.check_phone(memberVO.getmemId()).getmemPhoneNum())) {

			out.print("��ϵ� �ڵ�����ȣ�� �ƴմϴ�.");
			out.close();
		} else {
			// ������ȣ ����
			String Approval_key = "";
			for (int i = 0; i < 8; i++) {
				Approval_key += (char) ((Math.random() * 26) + 97);
			}
			memberVO.setApproval_key(Approval_key);
			// ������ȣ ����
			memberService.update_pw(memberVO);
			// ������ȣ ���� �߼�
			System.out.println("������ ��ȣ : " + memPhoneNum);
			System.out.println("������ȣ : " + Approval_key);
			// memberService.certifiedPhoneNumber(memPhoneNum,Approval_key);
			HttpSession session = request.getSession();
			session.setAttribute("memberPwd", memberVO);

			out.print("�ڵ�����ȣ�� ������ȣ�� �߼��Ͽ����ϴ�.");

			out.close();

		}
	}

	// ȸ������ �ڵ��� ����
	@RequestMapping(value = "join/check/sendSMS", method = RequestMethod.POST)
	public @ResponseBody void joinPhoneConf(String memPhoneNum, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// ������ȣ ����
		String Approval_key = "";
		for (int i = 0; i < 8; i++) {
			Approval_key += (char) ((Math.random() * 26) + 97);
		}

		System.out.println("������ ��ȣ : " + memPhoneNum);
		System.out.println("������ȣ : " + Approval_key);
		HttpSession session = request.getSession();
		session.setAttribute("memPhoneNum", memPhoneNum);
		session.setAttribute("Approval_key", Approval_key);
		// memberService.certifiedPhoneNumber(memPhoneNum,Approval_key);

		out.print("�ڵ�����ȣ�� ������ȣ�� �߼��Ͽ����ϴ�.");

		out.close();

	}

	// ȸ������ �ڵ��� ������ȣ Ȯ��
	@Override
	@RequestMapping(value = "/phone_confirm.do", method = RequestMethod.POST)
	public ResponseEntity phone_confirm(@RequestParam("Approval_key") String Approval_key, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {
		ResponseEntity resEntity = null;
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();

		HttpSession session = request.getSession();

		String memPhoneNum = (String) session.getAttribute("memPhoneNum");
		String session_Approval_key = (String) session.getAttribute("Approval_key");

		if (session_Approval_key.equals(Approval_key)) {
			String result = "false";
			resEntity = new ResponseEntity(result, HttpStatus.OK);
			return resEntity;

		} else {
			String result = "true";
			resEntity = new ResponseEntity(result, HttpStatus.OK);
			return resEntity;
		}

	}

	// �̸��� ������ȣ Ȯ��
	@Override
	@RequestMapping(value = "/email_confirm.do", method = RequestMethod.POST)
	public ModelAndView email_confirm(@RequestParam("Approval_key") String Approval_key, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String viewName = (String) request.getAttribute("viewName");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		ModelAndView mav = new ModelAndView();

		memberVO = memberService.email_confirm(Approval_key);
		HttpSession session = request.getSession();
		session.setAttribute("memberPwd", memberVO);
		if (memberVO == null) {
			out.println("<script>");
			out.println("alert('������ȣ�� ��ġ���� �ʽ��ϴ�.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return null;
		} else {
			mav.setViewName("redirect:/login_05.do");
			return mav;
		}

	}

	// �� ��й�ȣ
	@Override
	@RequestMapping(value = "/newPassWord.do", method = RequestMethod.POST)
	public ModelAndView newPassWord(@RequestParam("memPwd") String memPwd, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberPwd");
		memberVO.setmemPwd(memPwd);
		String pwd = memberVO.getmemPwd();
		String memPwd1 = pwdEncoder.encode(pwd);
		memberVO.setmemPwd(memPwd1);

		ModelAndView mav = new ModelAndView();
		System.out.println(memPwd1);
		int result = 0;
		result = memberService.newPassWord(memberVO);
		System.out.println(result);

		mav.setViewName("redirect:/login_06.do");
		return mav;
	}

	// īī���� �α���..
	@RequestMapping(value = "/kakaoLogin.do", produces = "application/json", method = RequestMethod.GET)
	public String kakaoLogin(Model model, @RequestParam("code") String code, RedirectAttributes ra, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonNode accessToken;

		// JsonNodeƮ�����·� ��ū�޾ƿ´�
		JsonNode jsonToken = KakaoAccessToken.getKakaoAccessToken(code);
		// ���� json��ü �� access_token�� �����´�
		accessToken = jsonToken.get("access_token");

		System.out.println("access_token : " + accessToken);
		// access_token�� ���� ����� ���� ��û
		JsonNode userInfo = KakaoUserInfo.getKakaoUserInfo(accessToken);

		// Get id
		String Id = userInfo.path("id").asText();

		String Name = null;

		// �������� īī������ �������� Get properties
		JsonNode properties = userInfo.path("properties");
		JsonNode kakao_account = userInfo.path("kakao_account");

		Name = properties.path("nickname").asText();

		System.out.println("memId : " + Id);
		System.out.println("memName : " + Name);

		MemberVO member = new MemberVO();
		member.setmemId(Id);
		member.setmemName(Name);
		memberVO = memberService.login_kakao(member);

		if (memberVO != null) {

			HttpSession session1 = request.getSession();
			session1.setAttribute("member", memberVO);
			session1.setAttribute("isLogOn", true);
			return "main";

		} else {

			// 4.�Ľ� �������� ����//���� ����
			model.addAttribute("Id", Id);

			model.addAttribute("Name", Name);
			model.addAttribute("result", userInfo);
			System.out.println(userInfo);

			return "join_04";
		}

	}

	// īī���� DB�� �߰������۾� �� �α����۾�
	@Override
	@RequestMapping(value = "/addMembers_kakao.do", method = RequestMethod.POST)
	public ModelAndView addMember_kakao(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		request.setCharacterEncoding("utf-8");
		int result = 0;
		result = memberService.addMember_kakao(member);

		// �α����۾�
		memberVO = memberService.login_kakao(member);

		if (memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			mav.setViewName("redirect:/join_02.do");
		} else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/login_01.do");
		}
		return mav;
	}

	// īī�� �α��ν� DB�� ���� ������ �߰����� ��ġ�� �ʰ� �ٷ� �α���
	@RequestMapping(value = "/login_kakao.do", method = RequestMethod.GET)
	public ModelAndView kakao_login(RedirectAttributes rAttr, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mav = new ModelAndView();
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		session.setAttribute("member", memberVO);
		session.setAttribute("isLogOn", true);
		mav.setViewName("redirect:/main.do");

		return mav;
	}

}
