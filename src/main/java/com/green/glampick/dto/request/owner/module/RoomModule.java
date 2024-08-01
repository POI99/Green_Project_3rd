package com.green.glampick.dto.request.owner.module;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomImageEntity;
import com.green.glampick.entity.RoomServiceEntity;
import com.green.glampick.entity.ServiceEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.repository.RoomServiceRepository;
import com.green.glampick.repository.ServiceRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


public class RoomModule {


    // 이미지가 들어있는가?
    public static void imgExist(List<MultipartFile> image) {
        if(image == null || image.get(0).isEmpty()){
            throw new CustomException(OwnerErrorCode.NF);
        }
    }

    // 인원 정보가 올바른가?
    public static void personnel(int standard, int max){
        if ((max - standard) < 0) {
            throw new CustomException(OwnerErrorCode.PE);
        }
    }

    // 객실 이미지 저장
    public static List<String> imgInsert(List<MultipartFile> image, long glampId, long roomId, CustomFileUtils customFileUtils) {
        List<String> imgName = new ArrayList<>();
        try {
            // 폴더 생성
            String path = makeFolder(glampId, roomId, customFileUtils);
            // 파일 저장
            imgName = makeImgName(image, path, customFileUtils);
        } catch (Exception e){
            throw new CustomException(OwnerErrorCode.FE);
        }
        return imgName;
    }
    private static String makeFolder(long glampId, long roomId, CustomFileUtils customFileUtils){
        String path = String.format("glamping/%s/room/%s", glampId, roomId);
        customFileUtils.makeFolders(path);
        return path;
    }
    private static List<String> makeImgName(List<MultipartFile> image, String roomPath, CustomFileUtils customFileUtils) throws Exception{
        List<String> roomImg = new ArrayList<>();
        for (MultipartFile file : image) {
            String imgName = customFileUtils.makeRandomFileName(file);
            String imgUrlName = String.format("/pic/%s/%s", roomPath, imgName);
            roomImg.add(imgUrlName);
            String target = String.format("%s/%s", roomPath, imgName);
            customFileUtils.transferTo(file, target);
        }
        return roomImg;
    }

    // 객실 이미지 엔티티 리스트로 저장
    public static List<RoomImageEntity> saveImage(List<String> roomImgName, RoomEntity roomId){
        List<RoomImageEntity> list = new ArrayList<>();
        for (String img : roomImgName) {
            RoomImageEntity item = new RoomImageEntity();
            item.setRoomId(roomId);
            item.setRoomImageName(img);
            list.add(item);
        }
        return list;
    }

    // 객실 서비스 엔티티 리스트로 저장
    public static List<RoomServiceEntity> saveService(List<Long> roomService, RoomEntity roomId, ServiceRepository serviceRepository){
        List<RoomServiceEntity> list = new ArrayList<>();
        for (Long service : roomService) {
            RoomServiceEntity item = new RoomServiceEntity();
            item.setRoom(roomId);
            ServiceEntity serviceEntity = serviceRepository.getReferenceById(service);
            item.setService(serviceEntity);
            list.add(item);
        }
        return list;
    }


}
