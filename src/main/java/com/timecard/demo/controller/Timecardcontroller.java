package com.timecard.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.timecard.demo.entity.TimeCardData;

import com.timecard.demo.repository.TimecardRepository;

@Controller
@RequestMapping("/timecard/")
public class Timecardcontroller {
	@Autowired
	private TimecardRepository timecardrepository;

	@GetMapping("showForm")
	public String showStudentForm(TimeCardData timecarddata) {
		return "addtimecard";
	}

	@InitBinder public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@GetMapping("list")
	public String timecard(Model model) {
		model.addAttribute("timecarddata", this.timecardrepository.findAll());
		return "index";
	}
	
	@RequestMapping(value="add",method = RequestMethod.GET)
	public String addStudentprocess(Model model) {
		model.addAttribute("timecarddata", new TimeCardData());
		return "addtimecard";
	}
	
	@PostMapping("add")
	public String addStudent(@Validated TimeCardData timecarddata, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "addtimecard";
		}

		this.timecardrepository.save(timecarddata);
		return "redirect:list";
	}
	
	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable ("id") long id, Model model) {
		TimeCardData timecarddata = this.timecardrepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid timecard id : " + id));

		model.addAttribute("timecarddata", timecarddata);
		return "update-timecard";
	}
	@PostMapping("update/{id}")
	public String updateTimecard(@PathVariable("id") long id, @Validated TimeCardData timecarddata, BindingResult result, Model model) {
		if(result.hasErrors()) {
			timecarddata.setId(id);
			return "update-timecard";
		}

		// update timecard
		timecardrepository.save(timecarddata);

		// get all ( with update)
		model.addAttribute("timecarddata", this.timecardrepository.findAll());
		return "index";
	}

	@GetMapping("delete/{id}")
	public String deleteTimecard(@PathVariable ("id") long id, Model model) {

		TimeCardData timecarddata = this.timecardrepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid timecard id : " + id));

		this.timecardrepository.delete(timecarddata);
		model.addAttribute("timecarddata", this.timecardrepository.findAll());
		return "index";

	}


}


