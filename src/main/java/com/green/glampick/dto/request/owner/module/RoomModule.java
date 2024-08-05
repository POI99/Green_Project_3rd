package com.green.glampick.dto.request.owner.module;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.request.owner.RoomPostRequestDto;
import com.green.glampick.entity.*;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.repository.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public static void personnelUpdate(Integer standard, Integer max){
        if(standard != null && max != null){
            personnel(standard, max);
        }
        if(standard != null && (standard < 2 || standard > 6)) {
            throw new CustomException(OwnerErrorCode.PUE);
        }
        if(max != null && (max < 2 || max > 6)) {
            throw new CustomException(OwnerErrorCode.PUE);
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

    // 수정 시 null 인 경우 기존값 넣어주기
    public static RoomPostRequestDto dtoNull(RoomPostRequestDto dto, RoomEntity entity) {
        if (dto.getRoomName() == null || dto.getRoomName().isEmpty()) {
            dto.setRoomName(entity.getRoomName());
        }
        if (dto.getPrice() == null || dto.getPrice() < 0) {
            dto.setPrice(entity.getRoomPrice());
        }
        if (dto.getPeopleNum() == null || dto.getPeopleNum() < 0) {
            dto.setPeopleNum(entity.getRoomNumPeople());
        }
        if (dto.getPeopleMax() == null || dto.getPeopleMax() < 0) {
            dto.setPeopleMax(entity.getRoomMaxPeople());
        }
        if (dto.getInTime() == null || dto.getInTime().isEmpty()) {
            dto.setInTime(entity.getCheckInTime());
        }
        if (dto.getOutTime() == null || dto.getOutTime().isEmpty()) {
            dto.setOutTime(entity.getCheckOutTime());
        }

        return dto;
    }

    // 사용자가 가진 글램핑과 입력받은 룸 pk 가 일치하는지 확인
    public static void isRoomIdOk(RoomRepository roomRepository, GlampingRepository glampingRepository,
                                  OwnerRepository ownerRepository, long roomId, Long ownerId) {
        GlampingEntity readGlamp = null;
        GlampingEntity glamp = null;
        RoomEntity room = null;
        try {
            OwnerEntity owner = ownerRepository.findByOwnerId(ownerId);
            readGlamp = glampingRepository.findByOwner(owner);
            room = roomRepository.findByRoomId(roomId);
            glamp = room.getGlamp();
        } finally {
            if (room == null || readGlamp != glamp) {
                throw new CustomException(OwnerErrorCode.NMR);
            }
        }
    }

    // 서비스 수정
    public static void updateService(List<Long> roomService, List<Long> inputService, RoomEntity room,
                                     RoomServiceRepository roomServiceRepository, ServiceRepository serviceRepository) {
        if(roomService.isEmpty()) {
            // 원래 서비스가 없었다.
            if(inputService != null) {
                roomServiceRepository.saveAll(saveService(inputService, room, serviceRepository));
            }
            return;
        }
        // 원래 서비스가 있다.
        if (inputService == null) {
            roomServiceRepository.deleteAllByRoom(room);
            return;
        }
        List<Long> matchList = roomService.stream().filter(o -> inputService.stream()
                .anyMatch(Predicate.isEqual(o))).toList();
        for(Long service : matchList) {
            roomService.remove(service);
            inputService.remove(service);
        }
        for(Long service : roomService) {
            RoomServiceEntity deleteEntity =
                    roomServiceRepository.findByServiceAndRoom(serviceRepository.getReferenceById(service), room);
            roomServiceRepository.delete(deleteEntity);
        }
        List<RoomServiceEntity> saveEntity = new ArrayList<>();
        for (Long service : inputService) {
            RoomServiceEntity entity = new RoomServiceEntity();
            entity.setService(serviceRepository.getReferenceById(service));
            entity.setRoom(room);
            saveEntity.add(entity);
        }
        roomServiceRepository.saveAll(saveEntity);
    }

    // 시간 형식 확인
    public static void isValidTime(String time) {
        String timePatter  = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
        Pattern pattern = Pattern.compile(timePatter);
        Matcher matcher = pattern.matcher(time);
        if( !matcher.matches()) {
            throw new CustomException(OwnerErrorCode.IT);
        }
    }

    // 해당 객실의 사진이 맞는지 확인
    public static void checkImgId(RoomEntity roomEntity, RoomImageEntity imageEntity){
        if(imageEntity.getRoomId() != roomEntity) {
            throw new CustomException(OwnerErrorCode.CFI);
        }
    }

    // 파일 삭제
    public static void deleteImageOne(Long imgId, RoomImageRepository roomImageRepository, CustomFileUtils customFileUtils) {
        RoomImageEntity entity = roomImageRepository.getReferenceById(imgId);
        String dbName = entity.getRoomImageName();
        try {
            String filePath = dbName.substring(5);
            File file = new File(String.format("%s%s", customFileUtils.uploadPath, filePath));
            file.delete();
        } catch (Exception e) {
            throw new CustomException(OwnerErrorCode.CF);
        }
        roomImageRepository.delete(entity);
    }

}
