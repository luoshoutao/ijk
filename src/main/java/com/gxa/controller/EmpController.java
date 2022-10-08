package com.gxa.controller;

import com.gxa.entity.Dept;
import com.gxa.entity.Emp;
import com.gxa.service.EmpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
public class EmpController {

    @Autowired
    private EmpService empService;

    @RequiresPermissions("emp:list")
    @GetMapping("/emps")
    public String list(ModelMap map){
        List<Emp> emps = this.empService.queryAll();
//      int i=5/0;
        map.addAttribute("emps",emps);
        return "emp/list";
    }

    @RequiresPermissions("emp:add")
    @GetMapping("/emp/preAdd")
    public String toAddPage(Map map){
        List<Dept> depts = this.empService.queryDepts();

        map.put("depts",depts);
        return "emp/add";
    }

    @RequiresPermissions("emp:add")
    @PostMapping("/emp")
    public String add(Emp emp){

        System.out.println(emp);
        this.empService.add(emp);


        return "redirect:/emps";
    }

    @RequiresPermissions("emp:update")
    @GetMapping("/emp/{empno}")
    public String toEdit(@PathVariable("empno") Integer empno,Map map){
        System.out.println(empno);

        Emp emp = this.empService.queryByEmpno(empno);
        map.put("emp",emp);

        List<Dept> depts = this.empService.queryDepts();
        map.put("depts",depts);

        return "emp/edit";
    }

    @RequiresPermissions("emp:update")
    @PutMapping("/emp")
    public String edit(Emp emp){
        this.empService.update(emp);

        return "redirect:/emps";
    }

    @RequiresPermissions("emp:delete")
    @DeleteMapping("/emp/{empno}")
    public String delete(@PathVariable("empno") Integer empno){

        this.empService.delete(empno);

        return "redirect:/emps";
    }
}
