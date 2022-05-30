package com.example.demo.Controller;

import com.example.demo.Service.ifcMergeService;
import com.example.demo.Service.ifcPostService;
import com.example.demo.Service.ifcGetService;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class IfcController {

    private final ifcPostService HelloService;
    private final ifcMergeService IfcMergeService;

    @Autowired
    public IfcController(ifcPostService IfcGetService, ifcMergeService ifcMergeService) {
        this.HelloService = IfcGetService;
        IfcMergeService = ifcMergeService;
    }
    @Value("${my.IFCPATH}")
    private String ifcPATH;

    @Value("${my.SCRIPTPATH}")
    private String scriptPATH;

    @Value("${my.TempFolderPath}")
    private String tempFolderPath;

    static public JsonBimServerClientFactory factory;
    static public BimServerClient client;

    {
        try {
            factory = new JsonBimServerClientFactory("http://localhost:8082");
            client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
        } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/postIfcAsSubProject")
    @ResponseBody
    public ResponseEntity.BodyBuilder postIfc(@RequestParam String fileName, String schema, Long parentPoid, String projectName){
       return ifcPostService.postIfc(fileName,ifcPATH,schema, parentPoid,projectName);
    }

    @GetMapping("/getIfc")
    @ResponseBody
    public ResponseEntity.BodyBuilder getIfc(@RequestParam Long fileName, String schema, String localName){
        return ifcGetService.downloadIfc(fileName,ifcPATH,schema,localName);}

    @GetMapping("/getProjectList")
    @ResponseBody
    public String getProjectList(){
        System.out.println(ifcGetService.authGetAllProjects(client));
        return ifcGetService.authGetAllProjects(client);
    }

    @GetMapping("/deleteProject")
    @ResponseBody
    public ResponseEntity.BodyBuilder deleteProject(@RequestParam Long oid){
        return ifcPostService.deleteProject(oid);
    }

    @PostMapping("/merge")
    @ResponseBody
    public ResponseEntity.BodyBuilder merge(@RequestParam(value = "file", required = false) MultipartFile file, Long mergeFile2) {
            return ifcMergeService.mergeIfc(file, mergeFile2 ,scriptPATH,tempFolderPath);}




}

