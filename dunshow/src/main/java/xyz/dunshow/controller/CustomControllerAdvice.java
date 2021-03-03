package xyz.dunshow.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import lombok.extern.slf4j.Slf4j;
import xyz.dunshow.constants.ErrorMessage;
import xyz.dunshow.dto.AjaxResponse;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.exception.PageException;

@ControllerAdvice
@Slf4j
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PageException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(PageException e, HttpServletRequest request, Model model) {
        log.info("PageException Handler", e);
        model.addAttribute("message", e.getMessage());
        return "/error/custom";
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle(BusinessException e, HttpServletRequest request, Model model) {
        log.info("BusinessException Handler", e);
        ModelAndView mv = new ModelAndView();
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setAttributesMap(new AjaxResponse(e.getCode(), e.getMessage()).getMap());
            mv.setView(view);
            return mv;
        }
        mv.setViewName("/error/custom");
        mv.addObject("message", e.getMessage());
        return mv;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle(RuntimeException e, HttpServletRequest request, Model model) {
        log.error("RuntimeException Handler", e);
        ModelAndView mv = new ModelAndView();
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
//            view.setAttributesMap(new AjaxResponse("ERROR", ErrorMessage.ERROR.getMessage()).getMap());
            view.setAttributesMap(new AjaxResponse("ERROR", e.getMessage()).getMap());
            mv.setView(view);
            return mv;
        }
        mv.setViewName("/error/500");
        mv.addObject("message", ErrorMessage.ERROR.getMessage());
        return mv;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();
        AjaxResponse result = new AjaxResponse("Invalid", "입력값이 올바르지 않습니다.");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            result.putError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
