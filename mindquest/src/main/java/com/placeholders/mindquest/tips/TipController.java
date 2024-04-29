package com.placeholders.mindquest.tips;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("tips")
public class TipController {

    @Autowired
    private TipDao tipDao;

    @GetMapping("{category}")
    public Tip getRandomTipByCategory(@PathVariable String category)
    {
        Tip tip = tipDao.findRandomTipByCategory(category);
        return tip;
    }

}
