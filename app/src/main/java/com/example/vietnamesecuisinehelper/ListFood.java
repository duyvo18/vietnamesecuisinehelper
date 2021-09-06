package com.example.vietnamesecuisinehelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.vietnamesecuisinehelper.Utilities.RecyclerView.ItemAdapter;
import com.example.vietnamesecuisinehelper.Utilities.RecyclerView.ItemObject;

import java.util.ArrayList;

public class ListFood extends AppCompatActivity {

    private RecyclerView rcvItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        rcvItems = findViewById(R.id.rcv_items_china);
        ItemAdapter itemAdapter = new ItemAdapter(this, getListItems());
        rcvItems.setAdapter(itemAdapter);
        rcvItems.setLayoutManager(new LinearLayoutManager(this));
    }
    private ArrayList<ItemObject> getListItems() {
        ArrayList<ItemObject> list = new ArrayList<>();
        list.add(new ItemObject(R.drawable.banhcanh,"Bánh Canh","Bánh canh là một món ăn Việt Nam. Bánh canh bao gồm nước dùng được nấu từ tôm, cá và giò heo thêm gia vị tùy theo từng loại bánh canh. Sợi bánh canh có thể được làm từ bột gạo, bột mì, bột năng hoặc bột sắn hoặc bột gạo pha bột sắn."));
        list.add(new ItemObject(R.drawable.banhcuon,"Bánh Cuốn","Bánh cuốn còn gọi là bánh mướt hay bánh ướt là tên gọi một loại thực phẩm làm từ bột gạo hấp tráng mỏng, cuộn tròn, bên trong độn nhân rau hoặc thịt."));
        list.add(new ItemObject(R.drawable.banhkhot,"Bánh Khọt","Bánh khọt là loại bánh Việt Nam làm từ bột gạo hoặc bột sắn, có nhân tôm, được nướng và ăn kèm với rau sống, ớt tươi, thường ăn với nước mắm pha ngọt, rất ít khi chấm nước sốt mắm tôm."));
        list.add(new ItemObject(R.drawable.banhmi,"Bánh mì","Bánh mì Việt Nam (hay còn gọi là bánh mì) là một món ăn của Việt Nam, bao gồm vỏ là một ổ bánh mì nướng có da giòn, ruột mềm, bên trong là phần nhân."));
        list.add(new ItemObject(R.drawable.banxeo,"Bánh xèo","Bánh xèo là một loại bánh phổ biến ở châu Á, phiên bản bánh xèo của Nhật Bản và Triều Tiên có bột bên ngoài, bên trong có nhân là tôm, thịt, giá đỗ, kim chi, khoai tây, hẹ, (bánh xèo Triều Tiên); tôm, thịt, cải thảo (Nhật Bản) được rán màu vàng, đúc thành hình tròn hoặc gấp lại thành hình bán nguyệt."));
        list.add(new ItemObject(R.drawable.bunbohue,"Bún bò huế","Bún bò là một trong những đặc sản của xứ Huế, mặc dù món bún này phổ biến trên cả ba miền ở Việt Nam và cả người Việt tại hải ngoại."));
        list.add(new ItemObject(R.drawable.bundaumamtom,"Bún đậu mắm tôm","Bún đậu mắm tôm là món ăn đơn giản, dân dã trong ẩm thực miền Bắc Việt Nam. Đây là món thường được dùng như bữa ăn nhẹ, ăn chơi."));
        list.add(new ItemObject(R.drawable.bunrieu,"Bún riêu","Bún riêu là món canh truyền thống của Việt Nam gồm nước kho và bún. Có một số loại bún riêu, bao gồm bún riêu cua, bún riêu cá và bún riêu ốc."));
        list.add(new ItemObject(R.drawable.canhchua,"Canh chua","Canh chua là tên gọi của những món ăn nhiều nước và có vị chua do được nấu bằng các nguyên liệu phối trộn với gia vị tạo chua."));
        list.add(new ItemObject(R.drawable.chaolong,"Cháo lòng","Cháo lòng là món cháo được nấu theo phương thức nấu cháo thông thường, trong sự kết hợp với nước dùng ngọt làm từ xương lợn hay nước luộc lòng lợn."));
        list.add(new ItemObject(R.drawable.comtam,"Cơm tấm","Cơm tấm là một món ăn Việt Nam có nguyên liệu chủ yếu từ gạo tấm. Dù có nhiều tên gọi ở các vùng miền khác nhau, tuy nhiên nguyên liệu và cách thức chế biến của món ăn trên gần như là giống nhau."));
        list.add(new ItemObject(R.drawable.goicuon,"Gỏi cuốn","Gỏi cuốn hay còn được gọi là nem cuốn, là một món ăn khá phổ biến ở Việt Nam. Gỏi cuốn có xuất xứ từ Miền nam Việt Nam với tên gọi là gỏi cuốn - bằng các nguyên liệu gồm rau xà lách,rau thơm, thịt luộc, tôm tươi.. tất cả được cuộn trong vỏ bánh tráng."));
        list.add(new ItemObject(R.drawable.hutieu,"Hủ tiếu","Hủ tiếu, còn được viết là hủ tíu, là món ăn dùng chế phẩm gạo dạng sợi của người Triều Châu và người Mân Nam, có nhiều điểm tương tự như sa hà phấn của người Quảng Đông."));
        list.add(new ItemObject(R.drawable.miquang,"Mì quảng","Mì Quảng là một món ăn đặc sản đặc trưng của Quảng Nam và Đà Nẵng, Việt Nam."));
        list.add(new ItemObject(R.drawable.pho,"Phở","Phở là một món ăn truyền thống của Việt Nam, có nguồn gốc từ Hà Nội và Nam Định, và được xem là một trong những món ăn tiêu biểu cho nền ẩm thực Việt Nam."));
        return list;
    }
}