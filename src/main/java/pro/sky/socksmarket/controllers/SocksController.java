package pro.sky.socksmarket.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.socksmarket.exception.BadColorException;
import pro.sky.socksmarket.exception.BadQuantityException;
import pro.sky.socksmarket.exception.BadSizeException;
import pro.sky.socksmarket.model.Color;
import pro.sky.socksmarket.model.Size;
import pro.sky.socksmarket.model.Socks;
import pro.sky.socksmarket.services.SocksService;


@RestController
@RequestMapping("/api/socks")

public class SocksController {
    SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping
    @Operation(description = "Регистрирует приход товара на склад")
    public ResponseEntity<String> addSocks(@RequestBody Socks socks, @PathVariable int quantity) {
        try {
            int current = socksService.add(socks, quantity);
            return ResponseEntity.ok().body("Добавлено " + socks + "в количестве"
                    + quantity + " пар." + "Теперь на складе " + current + " пар.");
        } catch (BadQuantityException e1) {
            return ResponseEntity.status(400).body("Количество должно быть строго больше нуля!");
        } catch (Exception e2) {
            return ResponseEntity.status(400).body("Носки " + socks + " не были добавлены");
        }
    }

    @PutMapping
    @Operation(description = "Регистрирует отпуск носков со склада")
    public ResponseEntity<String> takeSocks(@RequestBody Socks socks, @PathVariable int quantity) {
        try {
            socksService.take(socks, quantity);
            int current = socksService.getQuantity(socks);
            return ResponseEntity.ok().body("Реализовано " + socks + "в количестве"
                    + quantity + " пар." + "Теперь на складе " + current + " пар.");
        } catch (BadQuantityException e1) {
            return ResponseEntity.status(400).body("Количество должно быть строго больше нуля!");
        } catch (Exception e2) {
            return ResponseEntity.status(400).body("Носки " + socks + " не были реализованы");
        }
    }

    @DeleteMapping
    @Operation(description = "Регистрирует списание испорченных (бракованных) носков")
    public ResponseEntity<String> socksWriteOff(@RequestBody Socks socks, @PathVariable int quantity) {
        try {
            socksService.take(socks, quantity);
            int current = socksService.getQuantity(socks);
            return ResponseEntity.ok().body("Списано " + socks + "в количестве"
                    + quantity + " пар." + "Теперь на складе " + current + " пар.");
        }catch (BadQuantityException e1) {
            return ResponseEntity.status(400).body("Количество должно быть строго больше нуля!");
        } catch (Exception e2) {
            return ResponseEntity.status(400).body("Носки " + socks + " не были списаны");
        }
    }

    @GetMapping
    @Operation(description = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса")
    public ResponseEntity<String> getSocksInfo(@PathVariable String color, @PathVariable int size,
                                               @PathVariable int cottonMin, @PathVariable int cottonMax) {
        try {
            Color clr = stringToColor(color);
            Size sz = intToSize(size);
            int quantity = socksService.getInfo(clr, sz, cottonMin, cottonMax);
            return ResponseEntity.ok().body("На складе имеется"
                    + quantity + " пар подходящих носков.");

        } catch (BadColorException e1) {
            return ResponseEntity.status(400).body("Неверно указан цвет");
        } catch (BadSizeException e2) {
            return ResponseEntity.status(400).body("Неверно указан размер");
        } catch (Exception e3) {
            return ResponseEntity.status(500).body("Произошла ошибка, не зависящая от вызывающей стороны");
        }
    }


    private Color stringToColor(String color) {
        Color result = null;
        for (Color c : Color.values()) {
            if (c.toString().equalsIgnoreCase(color)) {
                result = c;
            }
        }
        if (result == null) throw new BadColorException();
        return result;
    }

    private Size intToSize(int size) {
        Size result = switch (size) {
            case 23 -> Size.S;
            case 24, 25 -> Size.M;
            case 26, 27 -> Size.L;
            case 28, 29 -> Size.XL;
            case 30, 31 -> Size.XXL;
            default -> null;
        };
        if (result == null) throw new BadSizeException();
        return result;
    }
}
