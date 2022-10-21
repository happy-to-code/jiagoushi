package com.yida.controller;

import com.yida.customer.annotation.RequestInitial;
import com.yida.domain.vo.AccountVO;
import com.yida.groups.Group;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/")
public class AnnoController {
	
	@PostMapping("anno")
	@RequestInitial(groups = {Group.Create.class})
	public String annoDemo(@Validated(Group.Create.class) @RequestBody AccountVO accountVO) {
		System.out.println("accountVO = " + accountVO);
		
		return "";
	}
}
