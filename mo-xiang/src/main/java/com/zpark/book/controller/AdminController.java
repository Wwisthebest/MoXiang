package com.zpark.book.controller;

import com.zpark.book.entity.IdleItem;
import com.zpark.book.entity.User;
import com.zpark.book.enums.ErrorMsg;
import com.zpark.book.entity.Admin;
import com.zpark.book.service.AdminService;
import com.zpark.book.service.IdleItemService;
import com.zpark.book.service.OrderService;
import com.zpark.book.service.UserService;
import com.zpark.book.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.zpark.book.enums.ErrorMsg.*;

@RestController
@RequestMapping("admin")

public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private IdleItemService idleItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResultVo login(@RequestBody Map<String, String> params, HttpSession session) {
        // 从params中获取参数
        String accountNumber = params.get("accountNumber");
        String adminPassword = params.get("adminPassword");

        // 参数校验
        if (accountNumber == null || accountNumber.isEmpty() ||
                adminPassword == null || adminPassword.isEmpty()) {
            return ResultVo.fail(ErrorMsg.PARAM_ERROR);
        }

        Admin admin = adminService.login(accountNumber, adminPassword);
        if (null == admin) {
            return ResultVo.fail(ErrorMsg.EMAIL_LOGIN_ERROR);
        }
        session.setAttribute("admin", admin);
        return ResultVo.success(admin);
    }

    @PostMapping("loginOut")
    public ResultVo loginOut( HttpSession session){
        session.removeAttribute("admin");
        return ResultVo.success();
    }

    @GetMapping("list")
    public ResultVo getAdminList(HttpSession session,
                                 @RequestParam(value = "page",required = false) Integer page,
                                 @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(adminService.getAdminList(p,n));
    }

    @PostMapping("add")
    public ResultVo addAdmin(HttpSession session,
                             @RequestBody Admin admin){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        if(adminService.addAdmin(admin)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.PARAM_ERROR);
    }

    @PostMapping("updateAdmin")
    public ResultVo updateAdmin(
            @RequestBody Admin updateModel){
        if(adminService.updateAdmin(updateModel)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.PARAM_ERROR);
    }

    @GetMapping("idleList")
    public ResultVo idleList(HttpSession session,
                             @RequestParam("status") @NotNull @NotEmpty Integer status,
                             @RequestParam(value = "page",required = false) Integer page,
                             @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(idleItemService.adminGetIdleList(status,p,n));
    }

    @GetMapping("getDetail")
    public ResultVo getDetail(HttpSession session,
                              @RequestParam("id") @NotNull @NotEmpty Long id){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        return ResultVo.success(idleItemService.getDetail(id));
    }

    @GetMapping("updateIdleStatus")
    public ResultVo updateIdleStatus(HttpSession session,
                                     @RequestParam("id") @NotNull @NotEmpty Long id,
                                     @RequestParam("status") @NotNull @NotEmpty Integer status
    ){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        IdleItem idleItem =new IdleItem();
        idleItem.setId(id);
        idleItem.setIdleStatus(status.byteValue());
        if(idleItemService.updateIdleItem(idleItem)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("orderList")
    public ResultVo orderList(HttpSession session,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(orderService.getAllOrder(p,n));
    }

    @GetMapping("deleteOrder")
    public ResultVo deleteOrder(HttpSession session,
                                @RequestParam("id") @NotNull @NotEmpty Long id){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        if(orderService.deleteOrder(id)){
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorMsg.SYSTEM_ERROR);
    }

    @GetMapping("userList")
    public ResultVo userList(HttpSession session,
                             @RequestParam(value = "page",required = false) Integer page,
                             @RequestParam(value = "nums",required = false) Integer nums,
                             @RequestParam("status") @NotNull @NotEmpty Integer status){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
        return ResultVo.success(userService.getUserByStatus(status,p,n));
    }

    // 用户状态管理接口 - 使用POST方法更符合RESTful规范
    @PostMapping("updateUserStatus")
    public ResultVo updateUserStatus(
            HttpSession session,
            @RequestParam("id") @NotNull @Min(1) Long userId, // 添加@Min验证
            @RequestParam("status") @NotNull @Min(0) @Max(1) Integer status) {

        // 验证管理员登录状态
        if (session.getAttribute("admin") == null) {
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }

        try {
            // 参数验证（JSR-303验证会先执行）
            // 执行封号逻辑
            boolean success = userService.updateUserStatus(userId, status.byteValue());
            if (success) {
                return ResultVo.success();
            } else {
                return ResultVo.fail(USER_NOT_EXIST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVo.fail(SERVER_ERROR);
        }
    }


    // 按订单闲置物品名称查询
    @GetMapping("queryIdle")
    public ResultVo queryIdle(@RequestParam(value = "findValue",required = false) String findValue,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "nums",required = false) Integer nums,
                              @RequestParam("status") @NotNull @NotEmpty Integer status){
        if(null==findValue){
            findValue="";
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }

        System.out.println(findValue + " " + page + " " + nums + " " + status);

        if(status == 1)
            return ResultVo.success(idleItemService.findIdleItem(findValue,p,n));
        return ResultVo.success(idleItemService.findIdleItem1(findValue,status,p,n));

    }

    // 按订单号查询订单
    @GetMapping("queryOrder")
    public ResultVo queryOrder(HttpSession session,
                               @RequestParam(value = "page",required = false) Integer page,
                               @RequestParam(value = "nums",required = false) Integer nums,
                               @RequestParam(value = "searchValue",required = false) String searchValue){

        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }

        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }

//        System.out.println("---------------------" + searchValue + "--------------");
        if(null == searchValue || "".equals(searchValue))
            return ResultVo.success(orderService.getAllOrder(p,n));
        return ResultVo.success(orderService.findOrderByNumber(searchValue, p, n));
    }




    // 根据用户账号来查找信息
    @GetMapping("queryUser")
    public ResultVo queryUser(HttpSession session,
                              @RequestParam(value = "searchValue",required = false) String searchValue,
                              @RequestParam(value = "mode",required = false) Integer mode,
                              @RequestParam(value = "page",required = false) Integer page,
                              @RequestParam(value = "nums",required = false) Integer nums){
        if(session.getAttribute("admin")==null){
            return ResultVo.fail(ErrorMsg.COOKIE_ERROR);
        }
        int p=1;
        int n=8;
        if(null!=page){
            p=page>0?page:1;
        }
        if(null!=nums){
            n=nums>0?nums:8;
        }
//        return ResultVo.success(userService.getUserByStatus(0,p,n));

        if(mode == 1){
            if(null == searchValue || "".equals(searchValue)){
                return ResultVo.success(userService.getUserByStatus(0,p,n));
            }else{
                return ResultVo.success(userService.getUserByNumber(searchValue,mode));
            }
        }else if(mode == 2){
            if(null == searchValue || "".equals(searchValue)){
                return ResultVo.success(userService.getUserByStatus(1,p,n));
            }else{
                return ResultVo.success(userService.getUserByNumber(searchValue,mode));
            }
        }else
            return ResultVo.success(adminService.getAdminList(p,n));



    }

}
