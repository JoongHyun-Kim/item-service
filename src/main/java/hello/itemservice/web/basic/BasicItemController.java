package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) { // "/basic/items" 경로로 GET이 오면 이 메소드가 호출
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items); //model에 "items"라고 컬렉션이 담긴다.

        return "basic/items"; //view 보여주기
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

//    @PostMapping("/add") //같은 url로 오더라도 Get, Post로 add와 save 구분
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {

        //파라미터가 들어오면 실제 객체를 생성해 담아준다.
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //ModelAttribute 버전
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {

        // ModelAttribute가 이 부분을 자동으로 처리
        /*
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        */

        //ModelAttribute는 1. model 객체를 만들어주고, 2. 내가 지정한 이름대로 (item) model에 담아준다.
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가. 생략 가능

        return "basic/item";
    }

    //@ModelAttribute의 이름 생략 버전
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {

        //클래스명의 첫글자를 소문자로 바꾼 것이 default값이 된다.
        //Item -> item
        itemRepository.save(item);
        return "basic/item";
    }

    //@ModelAttribute 생략 버전
    @PostMapping("/add")
    public String addItemV4(Item item) {

        itemRepository.save(item);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
