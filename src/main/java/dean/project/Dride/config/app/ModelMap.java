//package dean.project.Dride.config.app;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.TypeMap;
//
//public class ModelMap {
//    public static void main(String[] args) {
//        Man man = new Man("Dean", 34L);
//        ModelMapper modelMapper = new ModelMapper();
//        TypeMap<Man, ManObject> change = modelMapper.createTypeMap(man, ManObject.class);
//
//        change.addMappings(mapper -> mapper.map(Man::getName, ManObject::setMan));
//        System.out.println(change.getName());
//    }
//}
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//class Man {
//    private String name;
//    private Long age;
//}
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//class ManObject {
//    private String man;
//}
