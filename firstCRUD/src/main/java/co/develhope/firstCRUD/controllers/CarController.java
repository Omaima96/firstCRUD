package co.develhope.firstCRUD.controllers;

import co.develhope.firstCRUD.entities.Car;
import co.develhope.firstCRUD.repositories.CarRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
    @RequestMapping("/car")
    public class CarController {

        @Autowired
        private CarRepository carRepository;

        @PostMapping
        public Car create(@RequestBody Car car){
            car.setCarId(car.getCarId());
           Car carSaved = carRepository.saveAndFlush(car);
            return carSaved;
        }

        @GetMapping
        public Page<Car> get(@RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size){
            if(page.isPresent() && size.isPresent()) {
                Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "modelName"), new Sort.Order(Sort.Direction.DESC, "type"));
                Pageable pageable = PageRequest.of(page.get(), size.get(), sort);
                Page<Car> cars = carRepository.findAll(pageable);
                return cars;
            }else{
                Page<Car> pageCar = Page.empty();
                return pageCar;
            }
        }

        @GetMapping("/{id}")
        public Car getSingle(@PathVariable int id) {
            Car car = carRepository.getById(id);
            return car;}

    @GetMapping("/")
            public Optional<Car> getCarById(int id) {
                throwIfNotFoundId(id);
                return carRepository.findById(id);
            }

    private void throwIfNotFoundId(int id) {
        if (!carRepository.existsById(id)) {
            throw new IllegalArgumentException("Id not found ,the car is empty");
        }
}
      @PutMapping("/{id}")
        public Car updateSingle(@PathVariable String type, @RequestBody Car car){
            car.setType(type);
            Car newCar = carRepository.saveAndFlush(car);
            return newCar;
        }

        @DeleteMapping("/{id}")
        public void deleteSingle(@PathVariable int id){
            carRepository.deleteById(id);
        }

        @DeleteMapping("")
        public void deleteMultiple(@RequestParam List<Integer> ids){
            carRepository.deleteAllById(ids);
        }
    }

