package com.example.demo.Controller;

import com.example.demo.Entity.Bug;
import com.example.demo.Service.GetCodeImpl;
import com.example.demo.ServiceInterface.GetCodeService;
import com.example.demo.Tool.Group;
import com.example.demo.Tool.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GetCodeController implements GetCodeService {

    @Autowired
    private GetCodeImpl getcode;

    @RequestMapping(value = "/getcode", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public String getCodeService(String id, String path) {
        String code = getcode.getCodeService(id, path);
        return code;
    }

    @RequestMapping(value = "/probUpdate", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public void probUpdate(String id, double prob) {
        getcode.probUpdate(id, prob);
    }

    @RequestMapping(value = "/getbugs", method = RequestMethod.GET)
    @ResponseBody
    @Override

    public List<Bug> getBugs(String id) {
        return getcode.getBugs(id);
    }

    @RequestMapping(value = "/submitbug", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public void submitBug(String id, String type, String level, String comments) {
        getcode.submitBug(id, type, level, comments);
    }

    @RequestMapping(value = "/getstatics", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public List<Type> getstatics() {

        System.out.println("start");
        return getcode.getstatics();
    }

    @RequestMapping(value = "/getgroups", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public List<Group> getGroups() {
        return getcode.getGroups();
    }

    @RequestMapping(value = "/getonebug", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public Bug getOneBug(String id) {
        return getcode.getOneBug(id);
    }

    @RequestMapping(value = "/reviewpass", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public void reviewPass(String id) {
        getcode.reviewPass(id);
    }

    @RequestMapping(value = "/reviewfalse", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public void reviewFalse(String id) {
        getcode.reviewFalse(id);
    }


}
