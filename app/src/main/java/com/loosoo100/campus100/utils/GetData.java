package com.loosoo100.campus100.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loosoo100.campus100.R;
import com.loosoo100.campus100.beans.AddressInfo;
import com.loosoo100.campus100.beans.BorrowHistoryInfo;
import com.loosoo100.campus100.beans.BusOrderInfo;
import com.loosoo100.campus100.beans.BusRunsInfo;
import com.loosoo100.campus100.beans.BusinessInfo;
import com.loosoo100.campus100.beans.CampusContactsInfo;
import com.loosoo100.campus100.beans.CampusContactsLoveInfo;
import com.loosoo100.campus100.beans.CampusContactsMessageInfo;
import com.loosoo100.campus100.beans.CampusContactsReplyInfo;
import com.loosoo100.campus100.beans.CampusContactsUserInfo;
import com.loosoo100.campus100.beans.CampusInfo;
import com.loosoo100.campus100.beans.CashoutInfo;
import com.loosoo100.campus100.beans.CategoryInfo;
import com.loosoo100.campus100.beans.CityInfo;
import com.loosoo100.campus100.beans.CommunityActivityInfo;
import com.loosoo100.campus100.beans.CommunityBasicInfo;
import com.loosoo100.campus100.beans.CommunityMemberInfo;
import com.loosoo100.campus100.beans.CommunityMoney;
import com.loosoo100.campus100.beans.CommunityMoneyInfo;
import com.loosoo100.campus100.beans.ConsumeInfo;
import com.loosoo100.campus100.beans.CustomInfo;
import com.loosoo100.campus100.beans.DeliverInfo;
import com.loosoo100.campus100.beans.GiftCategoryInfo;
import com.loosoo100.campus100.beans.GiftGoodsAttr;
import com.loosoo100.campus100.beans.GiftGoodsAttrVal;
import com.loosoo100.campus100.beans.GiftGoodsInfo;
import com.loosoo100.campus100.beans.GiftGoodsStockInfo;
import com.loosoo100.campus100.beans.GiftOrderGoodsInfo;
import com.loosoo100.campus100.beans.GiftOrderInfo;
import com.loosoo100.campus100.beans.GiftTogetherFriendInfo;
import com.loosoo100.campus100.beans.GiftTogetherInfo;
import com.loosoo100.campus100.beans.GoodsInfo;
import com.loosoo100.campus100.beans.ImageInfo;
import com.loosoo100.campus100.beans.MessageInfo;
import com.loosoo100.campus100.beans.MyCollectInfo;
import com.loosoo100.campus100.beans.MyCreditInfo;
import com.loosoo100.campus100.beans.OnlineConsultInfo;
import com.loosoo100.campus100.beans.OverseasInfo;
import com.loosoo100.campus100.beans.OverseasQuestionInfo;
import com.loosoo100.campus100.beans.OverseasReplyInfo;
import com.loosoo100.campus100.beans.PointInfo;
import com.loosoo100.campus100.beans.QuestionDetailBean;
import com.loosoo100.campus100.beans.QuestionInfo;
import com.loosoo100.campus100.beans.QuestionListInfo;
import com.loosoo100.campus100.beans.RepaymentInfo;
import com.loosoo100.campus100.beans.StoreOrderInfo;
import com.loosoo100.campus100.beans.UserInfo;
import com.loosoo100.campus100.beans.VersionInfo;
import com.loosoo100.campus100.beans.WaterInfo;
import com.loosoo100.campus100.card.PictureWallInfo;
import com.loosoo100.campus100.utils.city.PinYin;
import com.loosoo100.campus100.zzboss.beans.BossCityInfo;
import com.loosoo100.campus100.zzboss.beans.BossCommSupportInfo;
import com.loosoo100.campus100.zzboss.beans.BossCompanyInfo;
import com.loosoo100.campus100.zzboss.beans.BossCompanySummaryInfo;

public class GetData {
    /**
     * 获取企业版关注的社团个数
     */
    public static int getBossCommCount(String urlString) {
        InputStream is = null;
        int count = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            count = jsonObject.optInt("count");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return count;
    }

    /**
     * 获取需求列表
     */
    public static List<BossCommSupportInfo> getBossNeedsInfos(String urlString) {
        InputStream is = null;
        BossCommSupportInfo supportInfo = null;
        List<BossCommSupportInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("aneedlist");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        supportInfo = new BossCommSupportInfo();
                        jsonObject = acti.getJSONObject(i);
                        supportInfo.setId(jsonObject.optString("aneed_id"));
                        supportInfo.setDemandName(jsonObject
                                .optString("aneed_name"));
                        supportInfo.setOffer(jsonObject
                                .optString("aneed_outline"));
                        supportInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("aneed_money", "0")));
                        supportInfo.setRaiseMoney(Float.valueOf(jsonObject
                                .optString("aneed_hasmoney", "0")));
                        supportInfo.setHeadthumb(jsonObject
                                .optString("aneed_headimgthumb"));
                        supportInfo.setSupportCount(jsonObject.optInt(
                                "aneed_zhichi", 0));
                        supportInfo.setStatus(jsonObject.optInt("aneed_status",
                                1));
                        supportInfo.setOverTime(MyUtils.getSqlDate(jsonObject
                                .optLong("aneed_time", 0)));
                        supportInfo.setClassify(jsonObject
                                .optInt("aneed_classify"));
                        supportInfo.setSchool(jsonObject
                                .optString("school_name"));
                        supportInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        supportInfo.setFree(jsonObject.optString("aneed_tian",
                                "0"));
                        list.add(supportInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取我的需求列表
     */
    public static List<BossCommSupportInfo> getMyNeedsInfos(String urlString) {
        InputStream is = null;
        BossCommSupportInfo supportInfo = null;
        List<BossCommSupportInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray need = jsonObject2.optJSONArray("need");
                if (need != null) {
                    for (int i = 0; i < need.length(); i++) {
                        supportInfo = new BossCommSupportInfo();
                        jsonObject = need.getJSONObject(i);
                        supportInfo.setId(jsonObject.optString("aneed_id"));
                        supportInfo.setDemandName(jsonObject
                                .optString("aneed_name"));
                        supportInfo.setOffer(jsonObject
                                .optString("aneed_outline"));
                        supportInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("aneed_money", "0")));
                        supportInfo.setRaiseMoney(Float.valueOf(jsonObject
                                .optString("aneed_hasmoney", "0")));
                        supportInfo.setHeadthumb(jsonObject
                                .optString("aneed_headimgthumb"));
                        supportInfo.setSupportCount(jsonObject.optInt(
                                "aneed_zhichi", 0));
                        supportInfo.setStatus(jsonObject.optInt("aneed_status",
                                1));
                        supportInfo.setOverTime(MyUtils.getSqlDate(jsonObject
                                .optLong("aneed_time", 0)));
                        supportInfo.setClassify(jsonObject
                                .optInt("aneed_classify"));
                        supportInfo.setSchool(jsonObject
                                .optString("school_name"));
                        supportInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        supportInfo.setFree(jsonObject.optString("aneed_tian",
                                "0"));
                        list.add(supportInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 调查问卷列表
     */
    public static List<QuestionListInfo> getQuestionListInfos(String urlString) {
        InputStream is = null;
        List<QuestionListInfo> list = new ArrayList<QuestionListInfo>();
        QuestionListInfo questionListInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject2 = new JSONObject(jsonString);
            JSONArray root = jsonObject2.optJSONArray("root");
            if (root != null) {
                for (int i = 0; i < root.length(); i++) {
                    jsonObject = root.getJSONObject(i);
                    questionListInfo = new QuestionListInfo();
                    questionListInfo.setTitle(jsonObject.optString("name"));
                    questionListInfo.setContent(jsonObject.optString("intro"));
                    questionListInfo.setQid(jsonObject.optString("id"));
                    questionListInfo.setStatus(jsonObject.optInt("status", 1));
                    list.add(questionListInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 调查问卷
     */
    public static QuestionInfo getQuestionInfo(String urlString) {
        InputStream is = null;
        QuestionInfo questionInfo = new QuestionInfo();
        QuestionDetailBean detailBean = null;
        List<QuestionDetailBean> list = new ArrayList<QuestionDetailBean>();
        JSONObject jsonObject = null;
        String answer = "";
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject2 = new JSONObject(jsonString);
            questionInfo.setName(jsonObject2.optString("name"));
            questionInfo.setConclusion(jsonObject2.optString("conclusion"));
            questionInfo.setDescription(jsonObject2.optString("description"));
            JSONArray details = jsonObject2.optJSONArray("detail");
            if (details != null) {
                for (int i = 0; i < details.length(); i++) {
                    jsonObject = details.getJSONObject(i);
                    detailBean = new QuestionDetailBean();
                    detailBean.setQid(jsonObject.optString("qid"));
                    detailBean.setQuest_order(jsonObject
                            .optString("quest_order"));
                    detailBean
                            .setQuest_type(jsonObject.optString("quest_type"));
                    detailBean.setQuestion(jsonObject.optString("question"));
                    JSONArray options = jsonObject.optJSONArray("options");
                    if (options != null) {
                        List<String> answers = new ArrayList<>();
                        for (int j = 0; j < options.length(); j++) {
                            answer = options.getJSONObject(j).optString(
                                    (j + 1) + "");
                            answers.add(answer);
                        }
                        detailBean.setOptions(answers);
                    }
                    list.add(detailBean);
                }
            }
            questionInfo.setDetail(list);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return questionInfo;
    }

    /**
     * 社团需求报名人员
     */
    public static List<UserInfo> getCommNeedMemJoinInfos(String urlString) {
        InputStream is = null;
        JSONObject jsonObject = null;
        UserInfo userInfo = null;
        List<UserInfo> list = new ArrayList<UserInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("status");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        jsonObject = acti.getJSONObject(i);
                        userInfo = new UserInfo();
                        userInfo.setNickName(jsonObject
                                .optString("company_name"));
                        userInfo.setSex(jsonObject
                                .optString("company_sex", "1"));
                        userInfo.setHeadShot(jsonObject.optString(
                                "company_logoThumb", ""));
                        userInfo.setName(jsonObject
                                .optString("company_trueName"));
                        userInfo.setPhone(jsonObject.optString("company_tel"));
                        userInfo.setOrg(jsonObject.optInt("actimoney_org", 1));
                        list.add(userInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取需求详情
     */
    public static BossCommSupportInfo getBossNeedsDetailsInfo(String urlString) {
        InputStream is = null;
        BossCommSupportInfo supportInfo = new BossCommSupportInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                supportInfo.setId(jsonObject.optString("aneed_id"));
                supportInfo.setSchoolID(jsonObject.optString("aneed_sid"));
                supportInfo.setDemandName(jsonObject.optString("aneed_name"));
                supportInfo.setOffer(jsonObject.optString("aneed_outline"));
                supportInfo.setNeedMoney(Float.valueOf(jsonObject.optString(
                        "aneed_money", "0")));
                supportInfo.setRaiseMoney(Float.valueOf(jsonObject.optString(
                        "aneed_hasmoney", "0")));
                supportInfo.setHeadthumb(jsonObject.optString("aneed_headimg"));
                supportInfo.setSupportCount(jsonObject
                        .optInt("aneed_zhichi", 0));
                supportInfo.setCollectCount(jsonObject.optInt("aneed_cang", 0));
                supportInfo.setReadCount(jsonObject.optInt("aneed_brower", 0));
                supportInfo.setStatus(jsonObject.optInt("aneed_status", 1));
                supportInfo.setOverTime(MyUtils.getSqlDate(jsonObject.optLong(
                        "aneed_time", 0)));
                supportInfo.setClassify(jsonObject.optInt("aneed_classify"));
                supportInfo.setSchool(jsonObject.optString("school_name"));
                supportInfo.setCity(jsonObject.optString("areaName")
                        + jsonObject.optString("city"));
                supportInfo.setFree(jsonObject.optString("aneed_tian", "0"));
                supportInfo.setLetter(jsonObject.optString("aneed_letter"));
                supportInfo.setCommId(jsonObject.optString("community_id"));
                supportInfo.setCommName(jsonObject.optString("community_name"));
                supportInfo.setCommHeadShot(jsonObject
                        .optString("community_logothumb"));
                supportInfo.setCommUid(jsonObject.optString("community_uid"));
                supportInfo.setCanyu(jsonObject.optInt("canyu"));
                supportInfo.setCollect(jsonObject.optInt("cangyn"));
                supportInfo.setFlag(jsonObject.optInt("flag",0));
                List<String> pics = new ArrayList<>();
                JSONArray array = jsonObject.optJSONArray("aneed_pics");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        pics.add(array.optString(i));
                    }
                }
                supportInfo.setPics(pics);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return supportInfo;
    }

    /**
     * 获取我的收藏需求
     */
    public static List<BossCommSupportInfo> getBossCollectNeedInfos(
            String urlString) {
        InputStream is = null;
        BossCommSupportInfo supportInfo = null;
        List<BossCommSupportInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray need = jsonObject2.optJSONArray("acti");
                if (need != null) {
                    for (int i = 0; i < need.length(); i++) {
                        supportInfo = new BossCommSupportInfo();
                        jsonObject = need.getJSONObject(i);
                        supportInfo.setId(jsonObject.optString("aneed_id"));
                        supportInfo.setDemandName(jsonObject
                                .optString("aneed_name"));
                        supportInfo.setOffer(jsonObject
                                .optString("aneed_outline"));
                        supportInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("aneed_money", "0")));
                        supportInfo.setRaiseMoney(Float.valueOf(jsonObject
                                .optString("aneed_hasmoney", "0")));
                        supportInfo.setHeadthumb(jsonObject
                                .optString("aneed_headimgthumb"));
                        supportInfo.setSupportCount(jsonObject.optInt(
                                "aneed_zhichi", 0));
                        supportInfo.setStatus(jsonObject.optInt("aneed_status",
                                1));
                        supportInfo.setOverTime(MyUtils.getSqlDate(jsonObject
                                .optLong("aneed_time", 0)));
                        supportInfo.setClassify(jsonObject
                                .optInt("aneed_classify"));
                        supportInfo.setSchool(jsonObject
                                .optString("school_name"));
                        supportInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        supportInfo.setFree(jsonObject.optString("aneed_tian",
                                "0"));
                        list.add(supportInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 城市数据
     */
    public static List<BossCityInfo> getAllCityInfos(String urlString) {
        InputStream is = null;
        List<BossCityInfo> list = new ArrayList<BossCityInfo>();
        BossCityInfo cityInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject2 = new JSONObject(jsonString);
            JSONArray allcity = jsonObject2.optJSONArray("allcity");
            if (allcity != null) {
                for (int i = 0; i < allcity.length(); i++) {
                    jsonObject = allcity.getJSONObject(i);
                    cityInfo = new BossCityInfo();
                    cityInfo.setCity(jsonObject.optString("areaName"));
                    cityInfo.setCityId(jsonObject.optString("areaId"));
                    cityInfo.setSortLetter(PinYin.getPinYin(jsonObject
                            .optString("areaName", "#")));
                    list.add(cityInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 按字母排序
        Collections.sort(list, new Comparator<BossCityInfo>() {
            @Override
            public int compare(BossCityInfo cityInfo1, BossCityInfo cityInfo2) {
                // 根据文本排序
                return ((String) cityInfo1.getSortLetter())
                        .compareTo((String) cityInfo2.getSortLetter());
            }
        });

        return list;
    }

    /**
     * 热门城市数据
     */
    public static List<BossCityInfo> getHotCityInfos(String urlString) {
        InputStream is = null;
        List<BossCityInfo> list = new ArrayList<BossCityInfo>();
        BossCityInfo cityInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject2 = new JSONObject(jsonString);
            JSONArray hotcity = jsonObject2.optJSONArray("hotcity");
            if (hotcity != null) {
                for (int i = 0; i < hotcity.length(); i++) {
                    jsonObject = hotcity.getJSONObject(i);
                    cityInfo = new BossCityInfo();
                    cityInfo.setCity(jsonObject.optString("areaName"));
                    cityInfo.setCityId(jsonObject.optString("areaId"));
                    list.add(cityInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取QQ状态数据
     */
    public static int getQQStatus(String urlString) {
        InputStream is = null;
        int status = 1;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            status = jsonObject.optInt("status", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

//    /**
//     * 企业是否关注社团
//     */
    public static int getCommFocusStatus(String urlString) {
        InputStream is = null;
        int status = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            status = jsonObject.optInt("is", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }
    /**
     * 判断是否社长
     */
    public static int getIsCommLeader(String urlString) {
        InputStream is = null;
        int status = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            status = jsonObject.optInt("status", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 获取送水数据
     */
    public static WaterInfo getWaterInfo(String urlString) {
        InputStream is = null;
        WaterInfo waterInfo = new WaterInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            waterInfo.setCount(jsonObject.optLong("num", 0));
            waterInfo.setStatus(jsonObject.optString("status", "0"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return waterInfo;
    }

    /**
     * 获取版本号
     */
    public static VersionInfo getvVersionInfo(String urlString) {
        InputStream is = null;
        VersionInfo versionInfo = new VersionInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            versionInfo.setVersion(jsonObject.optInt("version_num"));
            versionInfo.setUrl(jsonObject.optString("url",""));
            versionInfo.setVersionName(jsonObject.optString("version_name",""));
            versionInfo.setStatus(jsonObject.optInt("version_status",0));
            versionInfo.setDescription(jsonObject.optString("version_description",""));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return versionInfo;
    }

    /**
     * 海归求助评论列表
     */
    public static List<OverseasReplyInfo> getOverseasReplyInfos(String urlString) {
        InputStream is = null;
        List<OverseasReplyInfo> list = new ArrayList<OverseasReplyInfo>();
        OverseasReplyInfo overseasReplyInfo = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.optJSONArray("list");
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    overseasReplyInfo = new OverseasReplyInfo();
                    overseasReplyInfo.setName(jsonObject.optString("userName",
                            ""));
                    overseasReplyInfo.setId(jsonObject
                            .optString("hgans_id", ""));
                    overseasReplyInfo.setContent(jsonObject.optString(
                            "hgans_answer", ""));
                    overseasReplyInfo.setPid(jsonObject.optString("hgans_pid",
                            "0"));
                    overseasReplyInfo.setUid(jsonObject.optString("hgans_uid",
                            "0"));
                    overseasReplyInfo.setHeadShot(jsonObject
                            .optString("userPhoto"));
                    overseasReplyInfo.setSex(jsonObject.optString("userSex"));
                    if (jsonObject.optString("hgans_pid", "0").equals("0")) {
                        overseasReplyInfo.setPname("");
                    }
                    list.add(overseasReplyInfo);
                    JSONArray items = jsonObject.optJSONArray("items");
                    list = getItemsOverseasReply(items, list,
                            jsonObject.optString("userName", ""));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 递归方法 遍历所有回复
     *
     * @param items
     * @param appraiseList
     * @param pname
     * @return
     */
    private static List<OverseasReplyInfo> getItemsOverseasReply(
            JSONArray items, List<OverseasReplyInfo> appraiseList, String pname) {
        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = items.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OverseasReplyInfo replyInfo = new OverseasReplyInfo();
                replyInfo.setName(jsonObject.optString("userName", ""));
                replyInfo.setId(jsonObject.optString("hgans_id", ""));
                replyInfo.setContent(jsonObject.optString("hgans_answer", ""));
                replyInfo.setPid(jsonObject.optString("hgans_pid", "0"));
                replyInfo.setPname(pname);
                replyInfo.setSex(jsonObject.optString("userSex"));
                replyInfo.setUid(jsonObject.optString("hgans_uid", "0"));
                replyInfo.setHeadShot(jsonObject.optString("userPhoto"));
                appraiseList.add(replyInfo);
                JSONArray items2 = jsonObject.optJSONArray("items");
                if (items2 != null && items2.length() > 0) {
                    getItemsOverseasReply(items2, appraiseList,
                            jsonObject.optString("userName", ""));
                }
            }
        }
        return appraiseList;
    }

    /**
     * 海归求助回答信息
     */
    public static List<OverseasQuestionInfo> getOverseasQuestionInfos(
            String urlString) {
        InputStream is = null;
        List<OverseasQuestionInfo> list = new ArrayList<OverseasQuestionInfo>();
        OverseasQuestionInfo overseasQuestionInfo = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONArray jsonArray = object.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        overseasQuestionInfo = new OverseasQuestionInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        overseasQuestionInfo.setId(jsonObject
                                .optString("hgques_id"));
                        overseasQuestionInfo.setHeadShot(jsonObject
                                .optString("userPhoto"));
                        overseasQuestionInfo.setName(jsonObject
                                .optString("userName"));
                        overseasQuestionInfo.setDiscussCount(jsonObject.optInt(
                                "hgques_ans", 0));
                        overseasQuestionInfo.setQuestion(jsonObject
                                .optString("hgques_ques"));
                        overseasQuestionInfo.setReadingCount(jsonObject.optInt(
                                "hgques_read", 0));
                        overseasQuestionInfo.setTime(jsonObject
                                .optString("hgques_time"));
                        overseasQuestionInfo.setUid(jsonObject
                                .optString("hgques_uid"));
                        overseasQuestionInfo.setSex(jsonObject
                                .optString("userSex"));
                        list.add(overseasQuestionInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 海归学校信息
     */
    public static List<OverseasInfo> getOverseasInfos(String urlString) {
        InputStream is = null;
        OverseasInfo overseasInfo = null;
        List<OverseasInfo> list = new ArrayList<OverseasInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONArray jsonArray = object.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        overseasInfo = new OverseasInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        overseasInfo.setSchoolID(jsonObject
                                .optString("hgschool_id"));
                        overseasInfo.setSchoolName(jsonObject
                                .optString("hgschool_name"));
                        overseasInfo.setPicUrl(jsonObject
                                .optString("hgschool_img1thumb"));
                        overseasInfo.setStatus(jsonObject.optInt("cang", 0));
                        overseasInfo.setLocation(jsonObject
                                .optString("hgcoun_value"));
                        list.add(overseasInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 海归学校详情信息
     */
    public static OverseasInfo getOverseasInfo(String urlString) {
        InputStream is = null;
        OverseasInfo overseasInfo = new OverseasInfo();
        List<String> picList = new ArrayList<String>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                overseasInfo.setSchoolID(jsonObject.optString("hgschool_id"));
                overseasInfo.setEnterSchoolDifficulty(jsonObject.optInt(
                        "hgschool_diff", 1));
                overseasInfo.setFee(jsonObject.optString("hgschool_money"));
                overseasInfo.setFuture(jsonObject.optInt("hgschool_pros", 1));
                overseasInfo.setOurAppraise(jsonObject.optInt("hgschool_esti",
                        1));
                overseasInfo.setCollectCount(jsonObject.optLong(
                        "hgschool_cang", 0));
                overseasInfo.setSatisfaction(jsonObject.optInt(
                        "hgschool_satis", 1));
                overseasInfo.setSchoolName(jsonObject
                        .optString("hgschool_name"));
                overseasInfo.setSchoolNameENG(jsonObject
                        .optString("hgschool_english"));
                overseasInfo.setPicUrl01(jsonObject.optString("hgschool_img1"));
                overseasInfo.setPicUrl02(jsonObject.optString("hgschool_img2"));
                overseasInfo.setPicUrl03(jsonObject.optString("hgschool_img3"));
                overseasInfo.setStatus(jsonObject.optInt("cang", 0));
                overseasInfo.setOverLetter(jsonObject
                        .optString("hgschool_letter"));
                JSONArray hgschool_pics = jsonObject
                        .optJSONArray("hgschool_pics");
                if (hgschool_pics != null && hgschool_pics.length() > 0) {
                    for (int i = 0; i < hgschool_pics.length(); i++) {
                        picList.add(hgschool_pics.optString(i));
                    }
                }
                overseasInfo.setPicList(picList);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return overseasInfo;
    }

    /**
     * 获取首页轮播图
     */
    public static List<ImageInfo> getImageInfos(String urlString) {
        InputStream is = null;
        ImageInfo imageInfo = null;
        JSONObject jsonObject = null;
        List<ImageInfo> list = new ArrayList<>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                imageInfo = new ImageInfo();
                imageInfo.setUrl(jsonObject.optString("adfile", ""));
                imageInfo.setGoUrl(jsonObject.optString("adurl", ""));
                list.add(imageInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取用户基本信息
     */
    public static UserInfo getUserBasicInfo(String urlString) {
        InputStream is = null;
        UserInfo userInfo = new UserInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            userInfo.setName(jsonObject.optString("trueName", ""));
            userInfo.setWeixin(jsonObject.optString("weixin", ""));
            userInfo.setSex(jsonObject.optString("userSex", "1"));
            userInfo.setSchoolID(jsonObject.optString("school_shopid", ""));
            userInfo.setSchool(jsonObject.optString("school_name", ""));
            userInfo.setStuNo(jsonObject.optString("stuNo", ""));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userInfo;
    }

    /**
     * 获取头像等基本信息
     */
    public static UserInfo getUserInfo(String urlString) {
        InputStream is = null;
        UserInfo userInfo = new UserInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            userInfo.setHeadShot(jsonObject.optString("userPhoto", ""));
            userInfo.setNickName(jsonObject.optString("userName", ""));
            userInfo.setWeixin(jsonObject.optString("weixin", ""));
            userInfo.setSex(jsonObject.optString("userSex", "1"));
            userInfo.setMoney(jsonObject.optString("userMoney", "0"));
            userInfo.setPoint(jsonObject.optInt("userTotalScore"));
            userInfo.setSchoolID(jsonObject.optString("school_id", ""));
            userInfo.setSchool(jsonObject.optString("school_name", ""));
            userInfo.setStatus(jsonObject.optInt("status", 0));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userInfo;
    }

    /**
     * 获取企业版头像等基本信息
     */
    public static BossCompanyInfo getBossUserInfo(String urlString) {
        InputStream is = null;
        BossCompanyInfo companyInfo = new BossCompanyInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            companyInfo.setPhone(jsonObject.optString("company_phone"));
            companyInfo.setHeadShot(jsonObject.optString("company_photo", ""));
            companyInfo.setName(jsonObject.optString("company_trueName", ""));
            companyInfo.setWeixin(jsonObject.optString("weixin", ""));
            companyInfo.setSex(jsonObject.optString("company_sex", "1"));
            companyInfo.setCompany(jsonObject.optString("company_name", "0"));
            companyInfo.setFlag(jsonObject.optInt("company_flag", 0));
            companyInfo.setCuid(jsonObject.optString("company_id"));
            companyInfo.setNickName(jsonObject.optString("company_nickname"));
            companyInfo.setMoney(Float.valueOf(jsonObject.optString(
                    "company_money", "0")));
            companyInfo.setPoint(jsonObject.optInt("company_score", 0));
            companyInfo.setZanMoney(Float.valueOf(jsonObject.optString(
                    "company_zanmoney", "0")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return companyInfo;
    }

    /**
     * 获取省份或市区数据
     */
    public static List<CampusInfo> getCityInfos(String urlString) {
        List<CampusInfo> list = new ArrayList<CampusInfo>();
        CampusInfo campusInfo = null;
        InputStream is = null;
        JSONObject object = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    object = jsonArray.getJSONObject(i);
                    campusInfo = new CampusInfo();
                    campusInfo.setCampusName(object.optString("areaName"));
                    campusInfo.setCampusID(object.optString("areaId"));
                    list.add(campusInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 获取新建活动的分类列表
     */
    public static List<CampusInfo> getCommActiTypes(String urlString) {
        List<CampusInfo> list = new ArrayList<CampusInfo>();
        CampusInfo campusInfo = null;
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        campusInfo = new CampusInfo();
                        campusInfo.setCampusName(object
                                .optString("acti_carts_value"));
                        campusInfo.setCampusID(object
                                .optString("acti_carts_id"));
                        list.add(campusInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 获取银行列表数据
     */
    public static List<CampusInfo> getBankInfos(String urlString) {
        List<CampusInfo> list = new ArrayList<CampusInfo>();
        CampusInfo campusInfo = null;
        InputStream is = null;
        JSONObject object = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    object = jsonArray.getJSONObject(i);
                    campusInfo = new CampusInfo();
                    campusInfo.setCampusName(object.optString("bankName"));
                    campusInfo.setCampusID(object.optString("bankId"));
                    list.add(campusInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 获取社团成员审核数据
     */
    public static List<CommunityMemberInfo> getCommunityAuditingMemberInfos(
            String urlString) {
        List<CommunityMemberInfo> list = new ArrayList<CommunityMemberInfo>();
        CommunityMemberInfo communityMemberInfo = null;
        InputStream is = null;
        JSONObject object = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    object = jsonArray.getJSONObject(i);
                    communityMemberInfo = new CommunityMemberInfo();
                    communityMemberInfo.setUserId(object.optString("userId"));
                    communityMemberInfo.setName(object
                            .optString("trueName", ""));
                    communityMemberInfo.setSex(object.optInt("userSex"));
                    communityMemberInfo.setNickName(object.optString(
                            "userName", ""));
                    communityMemberInfo.setWeixin(object
                            .optString("weixin", ""));
                    // communityMemberInfo.setAge(object.optInt("userAge"));
                    communityMemberInfo.setHeadShotString(object.optString(
                            "userPhoto", ""));
                    list.add(communityMemberInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 获取社团资金记录信息
     */
    public static List<CommunityMoneyInfo> getCommunityMoneyInfos(
            String urlString) {
        List<CommunityMoneyInfo> list = new ArrayList<CommunityMoneyInfo>();
        CommunityMoneyInfo communityMoneyInfo = null;
        InputStream is = null;
        JSONObject object = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray listmore = jsonObject.optJSONArray("listmore");
                if (listmore != null) {
                    for (int i = 0; i < listmore.length(); i++) {
                        object = listmore.getJSONObject(i);
                        communityMoneyInfo = new CommunityMoneyInfo();
                        communityMoneyInfo.setMoneyChange(Float.valueOf(object
                                .optString("money", "0")));
                        communityMoneyInfo.setTime(MyUtils.getSqlDate(object
                                .optLong("comnfloatm_time")));
                        communityMoneyInfo.setMoney(Float.valueOf(object
                                .optString("comnfloatm_num", "0")));
                        communityMoneyInfo
                                .setType(object.optString("type", ""));
                        list.add(communityMoneyInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 获取社团资金
     */
    public static String getCommunityMoney(String urlString) {
        InputStream is = null;
        String money = "";
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                money = jsonObject.optString("money", "0");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return money;
    }

    /**
     * 我的社团数据
     */
    public static CommunityBasicInfo getMyCommunityBasicInfo(String urlString) {
        InputStream is = null;
        CommunityBasicInfo communityBasicInfo = new CommunityBasicInfo();
        CommunityBasicInfo communityBasicInfo2 = null;
        CommunityMoney communityMoney = null;
        List<CommunityActivityInfo> activityInfos = new ArrayList<CommunityActivityInfo>();
        List<CommunityBasicInfo> list = new ArrayList<CommunityBasicInfo>();
        List<BossCommSupportInfo> needList = new ArrayList<BossCommSupportInfo>();
        List<CommunityMoney> moneyList = new ArrayList<CommunityMoney>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                communityBasicInfo.setSchool(jsonObject
                        .optString("school_name"));
                communityBasicInfo.setCommunityName(jsonObject
                        .optString("community_name"));
                communityBasicInfo.setHeadthumb(jsonObject.optString(
                        "community_logothumb", ""));
                communityBasicInfo.setCommBg(jsonObject.optString(
                        "community_bg", ""));
                communityBasicInfo.setId(jsonObject.optString("community_id"));
                communityBasicInfo.setId2(jsonObject.optString("mid"));
                communityBasicInfo.setUserId(jsonObject.optString("userId"));
                communityBasicInfo.setName(jsonObject.optString("userName"));
                communityBasicInfo.setSlogan(jsonObject
                        .optString("community_kouhao"));
                communityBasicInfo.setNotice(jsonObject
                        .optString("community_notice"));
                communityBasicInfo.setSummary(jsonObject
                        .optString("community_info"));
                communityBasicInfo
                        .setDep(jsonObject.optString("community_dep"));
                communityBasicInfo.setType(jsonObject
                        .optString("community_type"));
                communityBasicInfo.setMoney(Float.valueOf(jsonObject.optString(
                        "community_money", "0")));
                communityBasicInfo.setMemCount(jsonObject.optInt(
                        "member_count", 0));

                JSONArray acti = jsonObject.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        JSONObject object = acti.getJSONObject(i);
                        CommunityActivityInfo activityInfo = new CommunityActivityInfo();
                        activityInfo.setId(object.optString("acti_id"));
                        activityInfo.setActivityName(object
                                .optString("acti_name"));
                        activityInfo.setSupportCount(object.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setNeedMoney(Float.valueOf(object
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(object
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setActiHeadShot(object.optString(
                                "acti_headimgthumb", ""));
                        activityInfo
                                .setClassify(object.optInt("acti_classify"));
                        activityInfo
                                .setDate(object.optString("acti_time", "0"));
                        activityInfo.setCity(object.optString("areaName")
                                + object.optString("city"));
                        activityInfo.setSchool(object.optString("school_name"));
                        activityInfo.setFree(object.optString("acti_tian"));
                        activityInfo.setStatus(object.optInt("acti_status", 1));
                        activityInfos.add(activityInfo);
                    }
                }
                communityBasicInfo.setActivityInfos(activityInfos);
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        communityMoney = new CommunityMoney();
                        communityMoney.setMoney(object
                                .optString("comntotalm_num"));
                        communityMoney.setTime(MyUtils.getSqlDateM(object
                                .optLong("comntotalm_time")));
                        moneyList.add(communityMoney);
                    }
                }
                communityBasicInfo.setMoneyList(moneyList);
                JSONArray comnlist = jsonObject.optJSONArray("comnlist");
                if (comnlist != null) {
                    for (int i = 0; i < comnlist.length(); i++) {
                        JSONObject object = comnlist.getJSONObject(i);
                        communityBasicInfo2 = new CommunityBasicInfo();
                        communityBasicInfo2.setId(object
                                .optString("community_id"));
                        communityBasicInfo2.setId2(object
                                .optString("comnmember_id"));
                        communityBasicInfo2.setCommunityName(object
                                .optString("community_name"));
                        list.add(communityBasicInfo2);
                    }
                }
                communityBasicInfo.setMyCommList(list);
                JSONArray need = jsonObject.optJSONArray("need");
                if (need != null) {
                    for (int i = 0; i < need.length(); i++) {
                        JSONObject needObject = need.getJSONObject(i);
                        BossCommSupportInfo supportInfo = new BossCommSupportInfo();
                        supportInfo.setId(needObject.optString("aneed_id"));
                        supportInfo.setDemandName(needObject
                                .optString("aneed_name"));
                        supportInfo.setOffer(needObject
                                .optString("aneed_outline"));
                        supportInfo.setNeedMoney(Float.valueOf(needObject
                                .optString("aneed_money", "0")));
                        supportInfo.setRaiseMoney(Float.valueOf(needObject
                                .optString("aneed_hasmoney", "0")));
                        supportInfo.setHeadthumb(needObject
                                .optString("aneed_headimgthumb"));
                        supportInfo.setSupportCount(needObject.optInt(
                                "aneed_zhichi", 0));
                        supportInfo.setStatus(needObject.optInt("aneed_status",
                                1));
                        supportInfo.setOverTime(MyUtils.getSqlDate(needObject
                                .optLong("aneed_time", 0)));
                        supportInfo.setClassify(needObject
                                .optInt("aneed_classify"));
                        supportInfo.setSchool(needObject
                                .optString("school_name"));
                        supportInfo.setCity(needObject.optString("areaName")
                                + needObject.optString("city"));
                        supportInfo.setFree(needObject.optString("aneed_tian",
                                "0"));
                        needList.add(supportInfo);
                    }
                }
                communityBasicInfo.setNeedInfos(needList);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return communityBasicInfo;
    }

    /**
     * 我的社团个数
     */
    public static int getMyCommunityCount(String urlString) {
        InputStream is = null;
        int count = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray comnlist = jsonObject.optJSONArray("comnlist");
            if (comnlist != null) {
                count = comnlist.length();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 我的其它社团数据
     */
    public static CommunityBasicInfo getMyCommunityOtherBasicInfo(
            String urlString) {
        InputStream is = null;
        CommunityMoney communityMoney = null;
        CommunityBasicInfo communityBasicInfo = new CommunityBasicInfo();
        List<CommunityActivityInfo> activityInfos = new ArrayList<CommunityActivityInfo>();
        List<CommunityMoney> moneyList = new ArrayList<CommunityMoney>();
        List<BossCommSupportInfo> needList = new ArrayList<BossCommSupportInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                communityBasicInfo.setSchool(jsonObject
                        .optString("school_name"));
                communityBasicInfo.setCommunityName(jsonObject
                        .optString("community_name"));
                communityBasicInfo.setHeadthumb(jsonObject.optString(
                        "community_logothumb", ""));
                communityBasicInfo.setCommBg(jsonObject.optString(
                        "community_bg", ""));
                communityBasicInfo.setId(jsonObject.optString("community_id"));
                communityBasicInfo.setUserId(jsonObject.optString("userId"));
                communityBasicInfo.setName(jsonObject.optString("userName"));
                communityBasicInfo.setSlogan(jsonObject
                        .optString("community_kouhao"));
                communityBasicInfo.setNotice(jsonObject
                        .optString("community_notice"));
                communityBasicInfo.setSummary(jsonObject
                        .optString("community_info"));
                communityBasicInfo
                        .setDep(jsonObject.optString("community_dep"));
                communityBasicInfo.setType(jsonObject
                        .optString("community_type"));
                communityBasicInfo.setMoney(Float.valueOf(jsonObject.optString(
                        "community_money", "0")));
                communityBasicInfo.setMemCount(jsonObject.optInt(
                        "member_count", 0));

                JSONArray acti = jsonObject.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        JSONObject object = acti.getJSONObject(i);
                        CommunityActivityInfo activityInfo = new CommunityActivityInfo();
                        activityInfo.setId(object.optString("acti_id"));
                        activityInfo.setActivityName(object
                                .optString("acti_name"));
                        activityInfo.setSupportCount(object.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setNeedMoney(Float.valueOf(object
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(object
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setActiHeadShot(object.optString(
                                "acti_headimgthumb", ""));
                        activityInfo
                                .setClassify(object.optInt("acti_classify"));
                        activityInfo
                                .setDate(object.optString("acti_time", "0"));
                        activityInfo.setCity(object.optString("areaName")
                                + object.optString("city"));
                        activityInfo.setSchool(object.optString("school_name"));
                        activityInfo.setFree(object.optString("acti_tian"));
                        activityInfo.setStatus(object.optInt("acti_status", 1));
                        activityInfos.add(activityInfo);
                    }
                }
                communityBasicInfo.setActivityInfos(activityInfos);
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        communityMoney = new CommunityMoney();
                        communityMoney.setMoney(object
                                .optString("comntotalm_num"));
                        communityMoney.setTime(MyUtils.getSqlDateM(object
                                .optLong("comntotalm_time")));
                        moneyList.add(communityMoney);
                    }
                }
                communityBasicInfo.setMoneyList(moneyList);
                JSONArray need = jsonObject.optJSONArray("need");
                if (need != null) {
                    for (int i = 0; i < need.length(); i++) {
                        JSONObject needObject = need.getJSONObject(i);
                        BossCommSupportInfo supportInfo = new BossCommSupportInfo();
                        supportInfo.setId(needObject.optString("aneed_id"));
                        supportInfo.setDemandName(needObject
                                .optString("aneed_name"));
                        supportInfo.setOffer(needObject
                                .optString("aneed_outline"));
                        supportInfo.setNeedMoney(Float.valueOf(needObject
                                .optString("aneed_money", "0")));
                        supportInfo.setRaiseMoney(Float.valueOf(needObject
                                .optString("aneed_hasmoney", "0")));
                        supportInfo.setHeadthumb(needObject
                                .optString("aneed_headimgthumb"));
                        supportInfo.setSupportCount(needObject.optInt(
                                "aneed_zhichi", 0));
                        supportInfo.setStatus(needObject.optInt("aneed_status",
                                1));
                        supportInfo.setOverTime(MyUtils.getSqlDate(needObject
                                .optLong("aneed_time", 0)));
                        supportInfo.setClassify(needObject
                                .optInt("aneed_classify"));
                        supportInfo.setSchool(needObject
                                .optString("school_name"));
                        supportInfo.setCity(needObject.optString("areaName")
                                + needObject.optString("city"));
                        supportInfo.setFree(needObject.optString("aneed_tian",
                                "0"));
                        needList.add(supportInfo);
                    }
                }
                communityBasicInfo.setNeedInfos(needList);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return communityBasicInfo;
    }

    /**
     * 我的社团获取社团成员数据
     */
    public static List<CommunityMemberInfo> getCommunityMemberInfos(
            String urlString) {
        InputStream is = null;
        CommunityMemberInfo memberInfo = null;
        List<CommunityMemberInfo> list = new ArrayList<CommunityMemberInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray member = jsonObject.optJSONArray("list");
                if (member != null) {
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject object = member.getJSONObject(i);
                        memberInfo = new CommunityMemberInfo();
                        memberInfo.setUserId(object.optString("userId"));
                        memberInfo.setSex(object.optInt("userSex"));
                        memberInfo.setName(object.optString("trueName", ""));
                        memberInfo.setWeixin(object.optString("weixin", ""));
                        memberInfo
                                .setNickName(object.optString("userName", ""));
                        memberInfo.setHeadShotString(object.optString(
                                "userPhoto", ""));
                        list.add(memberInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 企业版社团成员数据
     */
    public static List<CommunityMemberInfo> getBossCommMemberInfos(
            String urlString) {
        InputStream is = null;
        CommunityMemberInfo memberInfo = null;
        List<CommunityMemberInfo> list = new ArrayList<CommunityMemberInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray member = jsonObject.optJSONArray("list");
                if (member != null) {
                    for (int i = 0; i < member.length(); i++) {
                        JSONObject object = member.getJSONObject(i);
                        memberInfo = new CommunityMemberInfo();
                        memberInfo.setUserId(object.optString("userId"));
                        memberInfo.setSex(object.optInt("userSex"));
                        memberInfo
                                .setNickName(object.optString("userName", ""));
                        memberInfo.setName(object.optString("trueName", ""));
                        memberInfo.setHeadShotString(object.optString(
                                "userPhoto", ""));
                        list.add(memberInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团详情数据
     */
    public static CommunityBasicInfo getCommunityDetailInfos(String urlString) {
        InputStream is = null;
        CommunityBasicInfo communityBasicInfo = new CommunityBasicInfo();
        List<CommunityActivityInfo> activityInfos = new ArrayList<CommunityActivityInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                communityBasicInfo.setSchool(jsonObject
                        .optString("school_name"));
                communityBasicInfo.setUserId(jsonObject
                        .optString("userId"));
                communityBasicInfo.setCommunityName(jsonObject
                        .optString("community_name"));
                communityBasicInfo.setHeadthumb(jsonObject.optString(
                        "community_logothumb", ""));
                communityBasicInfo.setCommBg(jsonObject.optString(
                        "community_bg", ""));
                communityBasicInfo.setId(jsonObject.optString("community_id"));
                communityBasicInfo.setName(jsonObject.optString("userName"));
                communityBasicInfo.setSlogan(jsonObject
                        .optString("community_kouhao"));
                communityBasicInfo.setNotice(jsonObject
                        .optString("community_notice"));
                communityBasicInfo.setSummary(jsonObject
                        .optString("community_info"));
                communityBasicInfo
                        .setDep(jsonObject.optString("community_dep"));
                communityBasicInfo.setType(jsonObject
                        .optString("community_type"));
                communityBasicInfo.setIn(jsonObject.optString("m_in"));
                communityBasicInfo.setCang(jsonObject.optString("cangyn", "0"));
                communityBasicInfo.setMemCount(jsonObject.optInt(
                        "member_count", 0));
                communityBasicInfo.setFlag(jsonObject.optInt(
                        "flag", 0));

                JSONArray acti = jsonObject.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        JSONObject object = acti.getJSONObject(i);
                        CommunityActivityInfo activityInfo = new CommunityActivityInfo();
                        activityInfo.setId(object.optString("acti_id"));
                        activityInfo.setActivityName(object
                                .optString("acti_name"));
                        activityInfo.setSupportCount(object.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setNeedMoney(Float.valueOf(object
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(object
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setActiHeadShot(object.optString(
                                "acti_headimgthumb", ""));
                        activityInfo
                                .setClassify(object.optInt("acti_classify"));
                        activityInfo
                                .setDate(object.optString("acti_time", "0"));
                        activityInfo.setCity(object.optString("areaName")
                                + object.optString("city"));
                        activityInfo.setSchool(object.optString("school_name"));
                        activityInfo.setFree(object.optString("acti_tian"));
                        activityInfo.setStatus(object.optInt("acti_status", 1));
                        activityInfos.add(activityInfo);
                    }
                }
                communityBasicInfo.setActivityInfos(activityInfos);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return communityBasicInfo;
    }

    /**
     * 快递物流数据
     */
    public static DeliverInfo getDeliverInfo(String urlString) {
        InputStream is = null;
        DeliverInfo deliverInfo = new DeliverInfo();
        JSONObject jsonObject2 = null;
        Map<String, Object> map = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                deliverInfo.setNum(jsonObject.optString("nu"));
                deliverInfo.setCompany(jsonObject.optString("com"));
                JSONArray data = jsonObject.optJSONArray("data");
                if (data != null) {
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject2 = data.getJSONObject(i);
                        map = new HashMap<String, Object>();
                        map.put("context", jsonObject2.optString("context"));
                        map.put("time", jsonObject2.optString("time"));
                        list.add(map);
                    }
                }
                deliverInfo.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return deliverInfo;
    }

    /**
     * 社团基本数据
     */
    public static List<CommunityBasicInfo> getCommunityBasicInfos(
            String urlString) {
        InputStream is = null;
        CommunityBasicInfo communityBasicInfo = null;
        List<CommunityBasicInfo> list = new ArrayList<CommunityBasicInfo>();
        JSONObject jsonObject2 = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray comn = jsonObject.optJSONArray("comn");
                if (comn != null) {
                    for (int i = 0; i < comn.length(); i++) {
                        jsonObject2 = comn.getJSONObject(i);
                        communityBasicInfo = new CommunityBasicInfo();
                        communityBasicInfo.setId(jsonObject2
                                .optString("community_id"));
                        communityBasicInfo.setCommunityName(jsonObject2
                                .optString("community_name"));
                        communityBasicInfo.setHeadthumb(jsonObject2.optString(
                                "community_logothumb", ""));
                        communityBasicInfo.setSlogan(jsonObject2
                                .optString("community_kouhao"));
                        communityBasicInfo.setTime(MyUtils
                                .getSqlDate(jsonObject2.optLong(
                                        "community_time", 0)));
                        communityBasicInfo.setName(jsonObject2
                                .optString("userName"));
                        communityBasicInfo.setStatus(jsonObject2.optString(
                                "status", "1"));
                        list.add(communityBasicInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团活动报名人员
     */
    public static List<UserInfo> getCommMemJoinInfos(String urlString) {
        InputStream is = null;
        UserInfo userInfo = null;
        List<UserInfo> list = new ArrayList<UserInfo>();
        JSONObject jsonObject2 = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray mems = jsonObject.optJSONArray("acti");
                if (mems != null) {
                    for (int i = 0; i < mems.length(); i++) {
                        jsonObject2 = mems.getJSONObject(i);
                        userInfo = new UserInfo();
                        userInfo.setNickName(jsonObject2.optString("userName"));
                        userInfo.setSex(jsonObject2.optString("userSex", "1"));
                        userInfo.setHeadShot(jsonObject2.optString("userPhoto",
                                ""));
                        userInfo.setName(jsonObject2.optString("trueName"));
                        userInfo.setPhone(jsonObject2.optString("userPhone"));
                        userInfo.setOrg(jsonObject2.optInt("actimoney_org", 0));
                        list.add(userInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团活动数据
     */
    public static List<CommunityActivityInfo> getActivityInfos(String urlString) {
        InputStream is = null;
        CommunityActivityInfo communityActivityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<CommunityActivityInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject2.optJSONArray("acti");
                // JSONArray jsonArray = new JSONArray(jsonString);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        communityActivityInfo = new CommunityActivityInfo();
                        communityActivityInfo.setId(jsonObject
                                .optString("acti_id"));
                        communityActivityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        communityActivityInfo.setNeedMoney(Float
                                .valueOf(jsonObject
                                        .optString("acti_money", "0")));
                        communityActivityInfo.setRaisedMoney(Float
                                .valueOf(jsonObject.optString("acti_hasmoney",
                                        "0")));
                        communityActivityInfo.setSupportCount(jsonObject
                                .optInt("acti_zhichi"));
                        communityActivityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        communityActivityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb", ""));
                        communityActivityInfo.setFree(jsonObject.optString(
                                "acti_tian", "0"));
                        communityActivityInfo.setDate(jsonObject.optString(
                                "acti_time", "0"));
                        communityActivityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        communityActivityInfo.setCity(jsonObject
                                .optString("areaName")
                                + jsonObject.optString("city"));
                        communityActivityInfo.setStatus(jsonObject.optInt(
                                "acti_status", 1));
                        list.add(communityActivityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 赞助过的活动数据
     */
    public static List<CommunityActivityInfo> getBossMyActiInfos(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo communityActivityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<CommunityActivityInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        jsonObject = acti.getJSONObject(i);
                        communityActivityInfo = new CommunityActivityInfo();
                        communityActivityInfo.setId(jsonObject
                                .optString("acti_id"));
                        communityActivityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        communityActivityInfo.setNeedMoney(Float
                                .valueOf(jsonObject
                                        .optString("acti_money", "0")));
                        communityActivityInfo.setRaisedMoney(Float
                                .valueOf(jsonObject.optString("acti_hasmoney",
                                        "0")));
                        communityActivityInfo.setSupportCount(jsonObject
                                .optInt("acti_zhichi"));
                        communityActivityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        communityActivityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb", ""));
                        communityActivityInfo.setFree(jsonObject.optString(
                                "acti_tian", "0"));
                        communityActivityInfo.setDate(jsonObject.optString(
                                "acti_time", "0"));
                        communityActivityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        communityActivityInfo.setCity(jsonObject
                                .optString("areaName")
                                + jsonObject.optString("city"));
                        communityActivityInfo.setStatus(jsonObject.optInt(
                                "acti_status", 1));
                        list.add(communityActivityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团活动详情数据
     */
    public static CommunityActivityInfo getCommunityActivityInfo(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo communityActivityInfo = new CommunityActivityInfo();
        List<String> list = new ArrayList<String>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                communityActivityInfo.setCommId(jsonObject
                        .optString("community_id"));
                communityActivityInfo.setActivityName(jsonObject
                        .optString("acti_name"));
                communityActivityInfo.setName(jsonObject.optString("userName"));
                communityActivityInfo.setCommunityName(jsonObject
                        .optString("community_name"));
                communityActivityInfo.setSchool(jsonObject
                        .optString("school_name"));
                communityActivityInfo.setCommHeadShot(jsonObject.optString(
                        "community_logothumb", ""));
                communityActivityInfo.setNeedMoney(Float.valueOf(jsonObject
                        .optString("acti_money", "0")));
                communityActivityInfo.setRaisedMoney(Float.valueOf(jsonObject
                        .optString("acti_hasmoney", "0")));
                communityActivityInfo.setSupportCount(jsonObject
                        .optInt("acti_zhichi"));
                communityActivityInfo.setActiHeadShot(jsonObject.optString(
                        "acti_headimg", ""));
                communityActivityInfo.setStatus(jsonObject.optInt(
                        "acti_status", 1));
                communityActivityInfo.setRemainDay(Integer.valueOf(jsonObject
                        .optString("acti_yutian", "0")));
                communityActivityInfo.setActi_letter(jsonObject
                        .optString("acti_letter"));
                communityActivityInfo.setCollect(jsonObject.optInt("cang", 0));
                JSONArray acti_pics = jsonObject.optJSONArray("acti_pics");
                if (acti_pics != null) {
                    for (int i = 0; i < acti_pics.length(); i++) {
                        list.add(acti_pics.optString(i));
                    }
                }
                communityActivityInfo.setActi_pics(list);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return communityActivityInfo;
    }

    /**
     * 社团活动详情数据
     */
    public static CommunityActivityInfo getBossCommActiInfo(String urlString) {
        InputStream is = null;
        CommunityActivityInfo communityActivityInfo = new CommunityActivityInfo();
        List<String> list = new ArrayList<String>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                communityActivityInfo.setId(jsonObject.optString("acti_id"));
                communityActivityInfo.setActivityName(jsonObject
                        .optString("acti_name"));
                communityActivityInfo.setCommId(jsonObject
                        .optString("community_id"));
                communityActivityInfo.setClassify(jsonObject
                        .optInt("acti_classify"));
                communityActivityInfo.setStartTime(jsonObject.optLong(
                        "acti_holdtime1", 0));
                communityActivityInfo.setEndTime(jsonObject.optLong(
                        "acti_holdtime2", 0));
                communityActivityInfo.setCommunityName(jsonObject
                        .optString("community_name"));
                communityActivityInfo.setCommUid(jsonObject
                        .optString("community_uid"));
                communityActivityInfo.setSchool(jsonObject
                        .optString("school_name"));
                communityActivityInfo.setSchoolID(jsonObject
                        .optString("acti_shopid"));
                communityActivityInfo.setCommHeadShot(jsonObject.optString(
                        "community_logothumb", ""));
                communityActivityInfo.setNeedMoney(Float.valueOf(jsonObject
                        .optString("acti_money", "0")));
                communityActivityInfo.setRaisedMoney(Float.valueOf(jsonObject
                        .optString("acti_hasmoney", "0")));
                communityActivityInfo.setSupportCount(jsonObject
                        .optInt("acti_zhichi"));
                communityActivityInfo.setActiHeadShot(jsonObject.optString(
                        "acti_headimg", ""));
                communityActivityInfo
                        .setFree(jsonObject.optString("acti_tian"));
                communityActivityInfo.setStatus(jsonObject.optInt(
                        "acti_status", 1));
                communityActivityInfo.setRemainDay(Integer.valueOf(jsonObject
                        .optString("yutian", "0")));
                communityActivityInfo.setActi_letter(jsonObject
                        .optString("acti_letter"));
                communityActivityInfo.setCity(jsonObject.optString("areaName")
                        + jsonObject.optString("city"));
                communityActivityInfo.setCollectCount(jsonObject.optInt(
                        "acti_cang", 0));
                communityActivityInfo.setReadCount(jsonObject.optInt(
                        "acti_brower", 0));
                communityActivityInfo
                        .setCollect(jsonObject.optInt("cangyn", 0));
                communityActivityInfo.setCanyu(jsonObject.optInt("canyu", 0));
                communityActivityInfo.setAddress(jsonObject
                        .optString("acti_address"));
                communityActivityInfo
                        .setType(jsonObject.optString("acti_type"));
                communityActivityInfo
                        .setFlag(jsonObject.optInt("flag",0));
                JSONArray acti_pics = jsonObject.optJSONArray("acti_pics");
                if (acti_pics != null) {
                    for (int i = 0; i < acti_pics.length(); i++) {
                        list.add(acti_pics.optString(i));
                    }
                }
                communityActivityInfo.setActi_pics(list);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return communityActivityInfo;
    }

    /**
     * 社团活动列表数据
     */
    public static List<CommunityActivityInfo> getCommunityActivityInfos(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo communityActivityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<CommunityActivityInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray acti = jsonObject.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        jsonObject = acti.getJSONObject(i);
                        communityActivityInfo = new CommunityActivityInfo();
                        communityActivityInfo.setId(jsonObject
                                .optString("acti_id"));
                        communityActivityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        communityActivityInfo.setNeedMoney(Float
                                .valueOf(jsonObject
                                        .optString("acti_money", "0")));
                        communityActivityInfo.setRaisedMoney(Float
                                .valueOf(jsonObject.optString("acti_hasmoney",
                                        "0")));
                        communityActivityInfo.setSupportCount(jsonObject
                                .optInt("acti_zhichi"));
                        communityActivityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        communityActivityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb", ""));
                        communityActivityInfo.setFree(jsonObject.optString(
                                "acti_tian", "0"));
                        communityActivityInfo.setDate(jsonObject.optString(
                                "acti_time", "0"));
                        communityActivityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        communityActivityInfo.setCity(jsonObject
                                .optString("areaName")
                                + jsonObject.optString("city"));
                        communityActivityInfo.setStatus(jsonObject.optInt(
                                "acti_status", 1));
                        list.add(communityActivityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 校园爱心列表数据
     */
    public static List<CommunityActivityInfo> getHeartActivityInfos(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo activityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<CommunityActivityInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray acti = jsonObject.optJSONArray("status");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        jsonObject = acti.getJSONObject(i);
                        activityInfo = new CommunityActivityInfo();
                        activityInfo.setId(jsonObject.optString("acti_id"));
                        activityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        activityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb"));
                        activityInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(jsonObject
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setSupportCount(jsonObject.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setStatus(jsonObject.optInt("acti_status",
                                1));
                        activityInfo.setDate(jsonObject.optString("acti_time",
                                "0"));
                        activityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        activityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        activityInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        activityInfo.setFree(jsonObject.optString("acti_tian",
                                "0"));
                        list.add(activityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团活动筹集数据
     */
    public static List<GiftTogetherFriendInfo> getCommTogetherFriendInfos(
            String urlString) {
        InputStream is = null;
        JSONObject jsonObject = null;
        GiftTogetherFriendInfo commTogetherFriendInfo = null;
        List<GiftTogetherFriendInfo> list = new ArrayList<GiftTogetherFriendInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("status");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        jsonObject = acti.getJSONObject(i);
                        commTogetherFriendInfo = new GiftTogetherFriendInfo();
                        commTogetherFriendInfo.setContent(jsonObject.optString(
                                "actimoney_contents", ""));
                        commTogetherFriendInfo.setHeadShot(jsonObject
                                .optString("userPhoto", ""));
                        commTogetherFriendInfo.setMoney(Float
                                .valueOf(jsonObject.optString(
                                        "actimoney_money", "0")));
                        commTogetherFriendInfo.setName(jsonObject
                                .optString("userName"));
                        commTogetherFriendInfo.setTime(MyUtils
                                .getSqlDateLong(jsonObject.optLong(
                                        "actimoney_time", 0)));
                        commTogetherFriendInfo.setSex(jsonObject
                                .optString("userSex"));
                        commTogetherFriendInfo.setOrg(jsonObject
                                .optString("actimoney_org", ""));
                        list.add(commTogetherFriendInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团活动打赏数据
     */
    public static List<GiftTogetherFriendInfo> getCommSupportInfos(
            String urlString) {
        InputStream is = null;
        JSONObject jsonObject = null;
        GiftTogetherFriendInfo commTogetherFriendInfo = null;
        List<GiftTogetherFriendInfo> list = new ArrayList<GiftTogetherFriendInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("status");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        jsonObject = acti.getJSONObject(i);
                        commTogetherFriendInfo = new GiftTogetherFriendInfo();
                        commTogetherFriendInfo.setContent(jsonObject.optString(
                                "actimoney_contents", ""));
                        commTogetherFriendInfo.setHeadShot(jsonObject
                                .optString("company_logoThumb", ""));
                        commTogetherFriendInfo.setMoney(Float
                                .valueOf(jsonObject.optString(
                                        "actimoney_money", "0")));
                        commTogetherFriendInfo.setName(jsonObject
                                .optString("company_name"));
                        commTogetherFriendInfo.setTime(MyUtils
                                .getSqlDateLong(jsonObject.optLong(
                                        "actimoney_time", 0)));
                        commTogetherFriendInfo.setSex(jsonObject.optString(
                                "company_sex", "1"));
                        commTogetherFriendInfo.setOrg(jsonObject.optString(
                                "actimoney_org", ""));
                        list.add(commTogetherFriendInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团首页数据
     */
    public static List<CommunityBasicInfo> getCommunityHomeInfos(
            String urlString) {
        InputStream is = null;
        CommunityBasicInfo communityBasicInfo = null;
        List<CommunityBasicInfo> list = new ArrayList<CommunityBasicInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray comn = jsonObject.optJSONArray("comn");
                if (comn != null) {
                    for (int i = 0; i < comn.length(); i++) {
                        jsonObject = comn.getJSONObject(i);
                        communityBasicInfo = new CommunityBasicInfo();
                        communityBasicInfo.setId(jsonObject
                                .optString("community_id"));
                        communityBasicInfo.setCommunityName(jsonObject
                                .optString("community_name"));
                        communityBasicInfo.setHeadthumb(jsonObject.optString(
                                "community_logothumb", ""));
                        list.add(communityBasicInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 社团首页的顶部图片
     */
    public static String getCommImg(String urlString) {
        InputStream is = null;
        String imgUrl = "";
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                imgUrl = jsonObject.optString("school_pic", "");
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgUrl;
    }

    /**
     * 社团首页的状态，0代表管理社团，1代表可创建社团
     */
    public static int getCommStatus(String urlString) {
        InputStream is = null;
        int status = 1;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                status = jsonObject.optInt("status", 1);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 校园圈请求时间
     */
    public static long getCampusContactsTime(String urlString) {
        InputStream is = null;
        long nowTime = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            nowTime = jsonObject.optLong("nowTime", 0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nowTime;
    }

    /**
     * 校园圈消息请求时间
     */
    public static long getCampusContactsMessageTime(String urlString) {
        InputStream is = null;
        long nowTime = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            nowTime = jsonObject.optLong("time", 0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nowTime;
    }

    /**
     * 校园圈详情数据
     */
    public static CampusContactsInfo getCampusContactsInfo(String urlString) {
        InputStream is = null;
        CampusContactsInfo campusContactsInfo = null;
        List<CampusContactsReplyInfo> appraiseList = null;
        CampusContactsReplyInfo replyInfo = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONObject jsonObject = jsonObject2.getJSONObject("moments");
                campusContactsInfo = new CampusContactsInfo();

                if (jsonObject.optString("moments_id").equals("")) {
                    return null;
                }
                campusContactsInfo.setId(jsonObject.optString("moments_id"));
                campusContactsInfo.setUserId(jsonObject
                        .optString("moments_uid"));
                campusContactsInfo.setContent(jsonObject
                        .optString("moments_letter"));
                campusContactsInfo.setTime(jsonObject
                        .optLong("moments_time", 0));
                campusContactsInfo.setSchool(jsonObject
                        .optString("moments_sName"));
                campusContactsInfo.setSid(jsonObject.optString("moments_sid"));
                campusContactsInfo.setSex(jsonObject.optString("userSex", "1"));
                campusContactsInfo.setIsLiked(jsonObject.optInt("isZan"));
                campusContactsInfo.setLikeCount(jsonObject.optInt("zan", 0));
                // 图片集小
                List<String> picListThum = new ArrayList<>();
                if (!jsonObject.optString("mompic_pic1thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic1thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic2thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic2thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic3thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic3thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic4thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic4thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic5thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic5thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic6thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic6thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic7thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic7thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic8thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic8thumb", ""));
                }
                if (!jsonObject.optString("mompic_pic9thumb", "").equals("")) {
                    picListThum.add(jsonObject
                            .optString("mompic_pic9thumb", ""));
                }
                campusContactsInfo.setPicListThum(picListThum);
                // 图片集大
                ArrayList<String> picList = new ArrayList<>();
                if (!jsonObject.optString("mompic_pic1", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic1", ""));
                }
                if (!jsonObject.optString("mompic_pic2", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic2", ""));
                }
                if (!jsonObject.optString("mompic_pic3", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic3", ""));
                }
                if (!jsonObject.optString("mompic_pic4", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic4", ""));
                }
                if (!jsonObject.optString("mompic_pic5", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic5", ""));
                }
                if (!jsonObject.optString("mompic_pic6", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic6", ""));
                }
                if (!jsonObject.optString("mompic_pic7", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic7", ""));
                }
                if (!jsonObject.optString("mompic_pic8", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic8", ""));
                }
                if (!jsonObject.optString("mompic_pic9", "").equals("")) {
                    picList.add(jsonObject.optString("mompic_pic9", ""));
                }
                campusContactsInfo.setPicList(picList);

                campusContactsInfo.setHeadShot(jsonObject.optString(
                        "userPhoto", ""));
                campusContactsInfo
                        .setName(jsonObject.optString("userName", ""));

                // 评论列表
                JSONArray reply = jsonObject.optJSONArray("reply");
                if (reply != null && reply.length() > 0) {
                    appraiseList = new ArrayList<CampusContactsReplyInfo>();
                    for (int j = 0; j < reply.length(); j++) {
                        JSONObject jsonObject3 = reply.getJSONObject(j);
                        replyInfo = new CampusContactsReplyInfo();
                        replyInfo.setName(jsonObject3.optString(
                                "monreply_uname", ""));
                        replyInfo.setId(jsonObject3
                                .optString("momreply_id", ""));
                        replyInfo.setContent(jsonObject3.optString(
                                "momreply_letter", ""));
                        replyInfo.setPid(jsonObject3.optString("momreply_pid",
                                "0"));
                        replyInfo.setTime(MyUtils.getSqlDateMM(jsonObject3
                                .optLong("momreply_time", 0)));
                        replyInfo.setUid(jsonObject3.optString("momreply_uid",
                                "0"));
                        replyInfo.setHeadShot(jsonObject3
                                .optString("monreply_uPhoto"));
                        replyInfo.setSex(jsonObject.optString("userSex", "1"));
                        if (jsonObject3.optString("momreply_pid", "0").equals(
                                "0")) {
                            replyInfo.setPname("");
                        }
                        appraiseList.add(replyInfo);
                        JSONArray items = jsonObject3.optJSONArray("items");
                        appraiseList = getItemsDataTime(items, appraiseList,
                                jsonObject3.optString("monreply_uname", ""));
                    }
                    campusContactsInfo.setAppraiseList(appraiseList);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return campusContactsInfo;
    }

    /**
     * 校园圈查看过最新一条圈圈的发表时间
     */
    public static CampusContactsInfo getCampusContactsTimeInfo(String urlString) {
        InputStream is = null;
        CampusContactsInfo campusContactsInfo = new CampusContactsInfo();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray moments = jsonObject2.optJSONArray("moments");
                if (moments != null) {
                    jsonObject = moments.getJSONObject(0);
                    campusContactsInfo = new CampusContactsInfo();
                    campusContactsInfo.setTime(jsonObject.optLong(
                            "moments_time", 0));
                    campusContactsInfo.setHeadShot(jsonObject.optString(
                            "userPhoto", ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return campusContactsInfo;
    }

    /**
     * 校园圈数据
     */
    public static List<CampusContactsInfo> getCampusContactsInfos(
            String urlString) {
        InputStream is = null;
        List<CampusContactsInfo> list = new ArrayList<CampusContactsInfo>();
        CampusContactsInfo campusContactsInfo = null;
        List<CampusContactsReplyInfo> appraiseList = null;
        CampusContactsReplyInfo replyInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray moments = jsonObject2.optJSONArray("moments");
                if (moments != null) {
                    for (int i = 0; i < moments.length(); i++) {
                        jsonObject = moments.getJSONObject(i);
                        campusContactsInfo = new CampusContactsInfo();
                        campusContactsInfo.setId(jsonObject
                                .optString("moments_id"));
                        campusContactsInfo.setUserId(jsonObject
                                .optString("moments_uid"));
                        campusContactsInfo.setContent(jsonObject
                                .optString("moments_letter"));
                        campusContactsInfo.setTime(jsonObject.optLong(
                                "moments_time", 0));
                        campusContactsInfo.setSchool(jsonObject
                                .optString("moments_sName"));
                        campusContactsInfo.setIsLiked(jsonObject
                                .optInt("isZan"));
                        campusContactsInfo.setLikeCount(jsonObject.optInt(
                                "zan", 0));
                        // 图片集小
                        List<String> picListThum = new ArrayList<>();
                        if (!jsonObject.optString("mompic_pic1thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic1thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic2thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic2thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic3thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic3thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic4thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic4thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic5thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic5thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic6thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic6thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic7thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic7thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic8thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic8thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic9thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic9thumb", ""));
                        }
                        campusContactsInfo.setPicListThum(picListThum);
                        // 图片集大
                        ArrayList<String> picList = new ArrayList<>();
                        if (!jsonObject.optString("mompic_pic1", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic1", ""));
                        }
                        if (!jsonObject.optString("mompic_pic2", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic2", ""));
                        }
                        if (!jsonObject.optString("mompic_pic3", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic3", ""));
                        }
                        if (!jsonObject.optString("mompic_pic4", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic4", ""));
                        }
                        if (!jsonObject.optString("mompic_pic5", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic5", ""));
                        }
                        if (!jsonObject.optString("mompic_pic6", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic6", ""));
                        }
                        if (!jsonObject.optString("mompic_pic7", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic7", ""));
                        }
                        if (!jsonObject.optString("mompic_pic8", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic8", ""));
                        }
                        if (!jsonObject.optString("mompic_pic9", "").equals("")) {
                            picList.add(jsonObject.optString("mompic_pic9", ""));
                        }
                        campusContactsInfo.setPicList(picList);

                        campusContactsInfo.setHeadShot(jsonObject.optString(
                                "userPhoto", ""));
                        campusContactsInfo.setSex(jsonObject.optString(
                                "userSex", "0"));
                        campusContactsInfo.setName(jsonObject.optString(
                                "userName", ""));

                        // 评论列表
                        JSONArray reply = jsonObject.optJSONArray("reply");
                        if (reply != null && reply.length() > 0) {
                            appraiseList = new ArrayList<CampusContactsReplyInfo>();
                            for (int j = 0; j < reply.length(); j++) {
                                JSONObject jsonObject3 = reply.getJSONObject(j);
                                replyInfo = new CampusContactsReplyInfo();
                                replyInfo.setName(jsonObject3.optString(
                                        "monreply_uname", ""));
                                replyInfo.setId(jsonObject3.optString(
                                        "momreply_id", ""));
                                replyInfo.setContent(jsonObject3.optString(
                                        "momreply_letter", ""));
                                replyInfo.setPid(jsonObject3.optString(
                                        "momreply_pid", "0"));
                                replyInfo.setUid(jsonObject3.optString(
                                        "momreply_uid", ""));
                                replyInfo.setSex(jsonObject.optString(
                                        "userSex", "1"));
                                if (jsonObject3.optString("momreply_pid", "0")
                                        .equals("0")) {
                                    replyInfo.setPname("");
                                }
                                appraiseList.add(replyInfo);
                                JSONArray items = jsonObject3
                                        .optJSONArray("items");
                                appraiseList = getItemsData(items,
                                        appraiseList, jsonObject3.optString(
                                                "monreply_uname", ""));
                            }
                            campusContactsInfo.setAppraiseList(appraiseList);
                        }
                        list.add(campusContactsInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 递归方法 遍历所有回复
     *
     * @param items
     * @param appraiseList
     * @param pname
     * @return
     */
    private static List<CampusContactsReplyInfo> getItemsData(JSONArray items,
                                                              List<CampusContactsReplyInfo> appraiseList, String pname) {
        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = items.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CampusContactsReplyInfo replyInfo = new CampusContactsReplyInfo();
                replyInfo.setName(jsonObject.optString("monreply_uname", ""));
                replyInfo.setId(jsonObject.optString("momreply_id", ""));
                replyInfo.setContent(jsonObject
                        .optString("momreply_letter", ""));
                replyInfo.setPid(jsonObject.optString("momreply_pid", "0"));
                replyInfo.setPname(pname);
                replyInfo.setUid(jsonObject.optString("momreply_uid", "0"));
                replyInfo.setSex(jsonObject.optString("userSex", "1"));
                appraiseList.add(replyInfo);
                JSONArray items2 = jsonObject.optJSONArray("items");
                if (items2 != null && items2.length() > 0) {
                    getItemsData(items2, appraiseList,
                            jsonObject.optString("monreply_uname", ""));
                }
            }
        }
        return appraiseList;
    }

    /**
     * 递归方法 遍历所有回复,带时间
     *
     * @param items
     * @param appraiseList
     * @param pname
     * @return
     */
    private static List<CampusContactsReplyInfo> getItemsDataTime(
            JSONArray items, List<CampusContactsReplyInfo> appraiseList,
            String pname) {
        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = items.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CampusContactsReplyInfo replyInfo = new CampusContactsReplyInfo();
                replyInfo.setName(jsonObject.optString("monreply_uname", ""));
                replyInfo.setId(jsonObject.optString("momreply_id", ""));
                replyInfo.setContent(jsonObject
                        .optString("momreply_letter", ""));
                replyInfo.setPid(jsonObject.optString("momreply_pid", "0"));
                replyInfo.setPname(pname);
                replyInfo.setTime(MyUtils.getSqlDateMM(jsonObject.optLong(
                        "momreply_time", 0)));
                replyInfo.setUid(jsonObject.optString("momreply_uid", "0"));
                replyInfo.setHeadShot(jsonObject.optString("monreply_uPhoto"));
                replyInfo.setSex(jsonObject.optString("userSex", "1"));
                appraiseList.add(replyInfo);
                JSONArray items2 = jsonObject.optJSONArray("items");
                if (items2 != null && items2.length() > 0) {
                    getItemsDataTime(items2, appraiseList,
                            jsonObject.optString("monreply_uname", ""));
                }
            }
        }
        return appraiseList;
    }

    /**
     * 校园圈数据个人中心
     */
    public static List<CampusContactsInfo> getCampusContactsPersonInfos(
            String urlString) {
        InputStream is = null;
        List<CampusContactsInfo> list = new ArrayList<CampusContactsInfo>();
        CampusContactsInfo campusContactsInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray moments = jsonObject2.optJSONArray("moments");
                if (moments != null) {
                    for (int i = 0; i < moments.length(); i++) {
                        jsonObject = moments.getJSONObject(i);
                        campusContactsInfo = new CampusContactsInfo();
                        campusContactsInfo.setId(jsonObject.optString(
                                "moments_id", ""));
                        campusContactsInfo.setContent(jsonObject.optString(
                                "moments_letter", ""));
                        String time = MyUtils.getSqlDateM(jsonObject.optLong(
                                "moments_time", 0));
                        campusContactsInfo.setMonth(time.substring(0, 2));
                        campusContactsInfo.setDay(time.substring(3, 5));
                        campusContactsInfo.setIsLiked(jsonObject
                                .optInt("isZan"));
                        campusContactsInfo.setLikeCount(jsonObject.optInt(
                                "zan", 0));
                        // 图片集小
                        List<String> picListThum = new ArrayList<>();
                        if (!jsonObject.optString("mompic_pic1thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic1thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic2thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic2thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic3thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic3thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic4thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic4thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic5thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic5thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic6thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic6thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic7thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic7thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic8thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic8thumb", ""));
                        }
                        if (!jsonObject.optString("mompic_pic9thumb", "")
                                .equals("")) {
                            picListThum.add(jsonObject.optString(
                                    "mompic_pic9thumb", ""));
                        }
                        campusContactsInfo.setPicListThum(picListThum);
                        list.add(campusContactsInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 校园圈数据个人中心
     */
    public static CampusContactsUserInfo getCampusContactsUserInfo(
            String urlString) {
        InputStream is = null;
        CampusContactsUserInfo campusContactsUserInfo = new CampusContactsUserInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject users = jsonObject.optJSONObject("users");
                campusContactsUserInfo.setSex(users.optString("userSex", "1"));
                campusContactsUserInfo.setHeadShot(users.optString("userPhoto",
                        ""));
                campusContactsUserInfo.setBg(users.optString("moments_bg", ""));
                campusContactsUserInfo.setName(users.optString("userName"));
                campusContactsUserInfo
                        .setCrushNum(users.optLong("crushNum", 0));
                campusContactsUserInfo
                        .setSchool(users.optString("school_name"));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return campusContactsUserInfo;
    }

    /**
     * 校园圈判断是否屏蔽了该用户
     */
    public static int getCampusContactsStatus(String urlString) {
        InputStream is = null;
        int status = -1;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                status = jsonObject.optInt("status", -1);
            } catch (JSONException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    // /**
    // * 校园圈判断手机微信状态
    // */
    // public static CampusContactsStatus getCampusContactsStatus(String
    // urlString) {
    // InputStream is = null;
    // CampusContactsStatus campusContactsStatus = new CampusContactsStatus();
    // try {
    // is = new URL(urlString).openStream();
    // String jsonString = readStream(is);
    // try {
    // JSONObject jsonObject = new JSONObject(jsonString);
    // campusContactsStatus.setPhoneStatus(jsonObject.optInt("phone"));
    // campusContactsStatus.setWeixinStatus(jsonObject
    // .optInt("weixin"));
    // } catch (JSONException e) {
    // e.printStackTrace();
    // return null;
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // } finally {
    // try {
    // if (is != null) {
    // is.close();
    // is = null;
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // return campusContactsStatus;
    // }

    /**
     * 校园圈数据个人中心
     */
    public static List<CampusContactsMessageInfo> getCampusContactsMessageInfos(
            String urlString) {
        InputStream is = null;
        List<CampusContactsMessageInfo> list = new ArrayList<CampusContactsMessageInfo>();
        CampusContactsMessageInfo campusContactsMessageInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray repleyList = jsonObject2.optJSONArray("repleyList");
                if (repleyList != null) {
                    for (int i = 0; i < repleyList.length(); i++) {
                        jsonObject = repleyList.getJSONObject(i);
                        campusContactsMessageInfo = new CampusContactsMessageInfo();
                        campusContactsMessageInfo.setId(jsonObject.optString(
                                "momreply_id", ""));
                        campusContactsMessageInfo.setMid(jsonObject.optString(
                                "momreply_mid", ""));
                        campusContactsMessageInfo.setContent(jsonObject
                                .optString("momreply_letter", ""));
                        campusContactsMessageInfo.setType(jsonObject.optString(
                                "momreply_type", ""));
                        campusContactsMessageInfo.setTime(jsonObject.optLong(
                                "momreply_time", 0));
                        campusContactsMessageInfo.setName(jsonObject.optString(
                                "userName", ""));
                        campusContactsMessageInfo.setPicUrl(jsonObject
                                .optString("userPhoto", ""));
                        list.add(campusContactsMessageInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 校园圈暗恋数据
     */
    public static List<CampusContactsLoveInfo> getcCampusContactsLoveInfos(
            String urlString) {
        InputStream is = null;
        List<CampusContactsLoveInfo> list = new ArrayList<CampusContactsLoveInfo>();
        CampusContactsLoveInfo campusContactsLoveInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray crush_list = jsonObject2.optJSONArray("crush_list");
                if (crush_list != null) {
                    for (int i = 0; i < crush_list.length(); i++) {
                        jsonObject = crush_list.getJSONObject(i);
                        campusContactsLoveInfo = new CampusContactsLoveInfo();
                        campusContactsLoveInfo.setId(jsonObject
                                .optString("crush_id"));
                        campusContactsLoveInfo.setUid(jsonObject
                                .optString("crush_wid"));
                        campusContactsLoveInfo.setHeadShot(jsonObject
                                .optString("userPhoto", ""));
                        campusContactsLoveInfo.setName(jsonObject
                                .optString("userName"));
                        campusContactsLoveInfo.setSchool(jsonObject
                                .optString("school_name"));
                        campusContactsLoveInfo.setSex(jsonObject.optString(
                                "userSex", "1"));
                        campusContactsLoveInfo.setStatus(jsonObject
                                .optString("crush_content"));
                        list.add(campusContactsLoveInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 校园圈我的友友数据
     */
    public static List<CampusContactsLoveInfo> getCampusContactsFriendInfos(
            String urlString) {
        InputStream is = null;
        List<CampusContactsLoveInfo> list = new ArrayList<CampusContactsLoveInfo>();
        CampusContactsLoveInfo campusContactsLoveInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        campusContactsLoveInfo = new CampusContactsLoveInfo();
                        campusContactsLoveInfo.setUid(jsonObject
                                .optString("userId"));
                        campusContactsLoveInfo.setHeadShot(jsonObject
                                .optString("userPhoto", ""));
                        campusContactsLoveInfo.setName(jsonObject
                                .optString("userName"));
                        campusContactsLoveInfo.setTureName(jsonObject
                                .optString("trueName"));
                        campusContactsLoveInfo.setSchool(jsonObject
                                .optString("school_name"));
                        campusContactsLoveInfo.setSex(jsonObject.optString(
                                "userSex", "1"));
                        campusContactsLoveInfo.setWeixin(jsonObject.optString(
                                "weixin", ""));
                        campusContactsLoveInfo.setPhone(jsonObject.optString(
                                "userPhone", ""));
                        list.add(campusContactsLoveInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 校园圈我的友友请求数据
     */
    public static List<CampusContactsLoveInfo> getCampusContactsFriendReqInfos(
            String urlString) {
        InputStream is = null;
        List<CampusContactsLoveInfo> list = new ArrayList<CampusContactsLoveInfo>();
        CampusContactsLoveInfo campusContactsLoveInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject2.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        campusContactsLoveInfo = new CampusContactsLoveInfo();
                        campusContactsLoveInfo.setChangeId(jsonObject
                                .optString("change_id"));
                        campusContactsLoveInfo.setUid(jsonObject
                                .optString("userId"));
                        campusContactsLoveInfo.setHeadShot(jsonObject
                                .optString("userPhoto", ""));
                        campusContactsLoveInfo.setName(jsonObject
                                .optString("userName"));
                        campusContactsLoveInfo.setTureName(jsonObject
                                .optString("trueName"));
                        campusContactsLoveInfo.setSchool(jsonObject
                                .optString("school_name"));
                        campusContactsLoveInfo.setSex(jsonObject.optString(
                                "userSex", "1"));
                        campusContactsLoveInfo.setRequestType(jsonObject
                                .optString("change_type"));
                        list.add(campusContactsLoveInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取系统消息没读的条数和消息内容时间
     */
    public static int getNoReadCount(String urlString) {
        InputStream is = null;
        int count = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject = new JSONObject(jsonString);
            count = jsonObject.optInt("status", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 新的好友请求状态
     */
    public static int getFriendNoRead(String urlString) {
        InputStream is = null;
        int status = 1;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                status = jsonObject.optInt("status", 1);
            } catch (JSONException e) {
                e.printStackTrace();
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 校园圈我的友友和暗恋的人的未读消息个数
     */
    public static int getCampusContactsNoRead(String urlString) {
        InputStream is = null;
        int status = 0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                status = jsonObject.optInt("status", 0);
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * 校园包车订单数据
     */
    public static List<BusOrderInfo> busOrderInfos(Context context) {
        List<BusOrderInfo> list = new ArrayList<BusOrderInfo>();
        BusOrderInfo busOrderInfo = null;

        for (int i = 0; i < 8; i++) {
            busOrderInfo = new BusOrderInfo();
            busOrderInfo.setStartingPoint("广州大学");
            if (i > 3) {
                busOrderInfo.setDestination("深圳大学");
            } else {
                busOrderInfo.setDestination("北京大学");
            }
            busOrderInfo.setTime((i + 7) + ":00");
            busOrderInfo.setDate("2016-05-" + (12 - i));
            busOrderInfo.setPrice(66f);
            busOrderInfo.setOrderTime("2016-05-" + (11 - i));

            busOrderInfo.setName("花果山水帘洞美猴王齐天大圣孙悟空");
            busOrderInfo.setId("66666666666666666666");
            busOrderInfo.setPhone("13666666666");
            busOrderInfo.setWeixin("jay666");
            busOrderInfo.setOrderNumber("8888888888888888" + i);
            if (i > 1) {
                busOrderInfo.setStatus(0);
            } else {
                busOrderInfo.setStatus(1);
            }

            list.add(busOrderInfo);
        }
        return list;
    }

    /**
     * 校园包车班次数据
     */
    public static List<BusRunsInfo> busRunsInfos(Context context,
                                                 String startSchool, String endSchool) {
        List<BusRunsInfo> list = new ArrayList<BusRunsInfo>();
        BusRunsInfo busRunsInfo = null;

        for (int i = 0; i < 8; i++) {
            busRunsInfo = new BusRunsInfo();
            busRunsInfo.setTime((i + 7) + ":00");
            busRunsInfo.setStartSchool(startSchool);
            busRunsInfo.setEndSchool(endSchool);
            busRunsInfo.setPrice(66f);
            list.add(busRunsInfo);
        }
        return list;
    }

    /**
     * 愿望墙首页数据
     */
    public static List<PictureWallInfo> getPictureWallHomeInfos(String urlString) {
        InputStream is = null;
        PictureWallInfo pictureWallInfo = null;
        List<PictureWallInfo> list = new ArrayList<PictureWallInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray wishlist = jsonObject2.optJSONArray("wishlist");
                if (wishlist != null) {
                    for (int i = 0; i < wishlist.length(); i++) {
                        jsonObject = wishlist.getJSONObject(i);
                        pictureWallInfo = new PictureWallInfo();
                        pictureWallInfo.setId(jsonObject
                                .optString("wishing_id"));
                        pictureWallInfo.setDream(jsonObject
                                .optString("wishing_content"));
                        pictureWallInfo.setPicture(jsonObject
                                .optString("wishing_picthums"));
                        list.add(pictureWallInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 愿望墙数据
     */
    public static List<PictureWallInfo> getPictureWallInfos(String urlString) {
        InputStream is = null;
        PictureWallInfo pictureWallInfo = null;
        List<PictureWallInfo> list = new ArrayList<PictureWallInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray wishlist = jsonObject2.optJSONArray("wishlist");
                if (wishlist != null) {
                    for (int i = 0; i < wishlist.length(); i++) {
                        jsonObject = wishlist.getJSONObject(i);
                        pictureWallInfo = new PictureWallInfo();
                        pictureWallInfo.setId(jsonObject
                                .optString("wishing_id"));
                        pictureWallInfo.setSid(jsonObject
                                .optString("wishing_sid"));
                        pictureWallInfo.setDream(jsonObject
                                .optString("wishing_content"));
                        pictureWallInfo.setHeadShot(jsonObject
                                .optString("userPhoto"));
                        pictureWallInfo.setName(jsonObject
                                .optString("userName"));
                        pictureWallInfo.setTime(jsonObject
                                .optString("wishing_time"));
                        pictureWallInfo.setOpposeCount(Integer
                                .valueOf(jsonObject.optString("wishing_oppose",
                                        "0")));
                        pictureWallInfo.setPicture(jsonObject
                                .optString("wishing_pic"));
                        pictureWallInfo.setSchool(jsonObject
                                .optString("school_name"));
                        pictureWallInfo.setSupportCount(Integer
                                .valueOf(jsonObject.optString(
                                        "wishing_support", "0")));
                        pictureWallInfo.setUid(jsonObject
                                .optString("wishing_uid"));
                        pictureWallInfo.setUserSex(jsonObject
                                .optString("userSex"));
                        pictureWallInfo.setAction(jsonObject
                                .optString("action"));
                        list.add(pictureWallInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 我的愿望墙管理数据
     */
    public static List<PictureWallInfo> getPictureWallManageInfos(
            String urlString) {
        InputStream is = null;
        PictureWallInfo pictureWallInfo = null;
        List<PictureWallInfo> list = new ArrayList<PictureWallInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray wishlist = jsonObject2.optJSONArray("wishlist");
                if (wishlist != null) {
                    for (int i = 0; i < wishlist.length(); i++) {
                        jsonObject = wishlist.getJSONObject(i);
                        pictureWallInfo = new PictureWallInfo();
                        pictureWallInfo.setId(jsonObject
                                .optString("wishing_id"));
                        pictureWallInfo.setDream(jsonObject
                                .optString("wishing_content"));
                        pictureWallInfo.setPicture(jsonObject
                                .optString("wishing_pic"));
                        pictureWallInfo.setTime(jsonObject
                                .optString("wishing_time"));
                        pictureWallInfo.setSupportCount(Integer
                                .valueOf(jsonObject.optString(
                                        "wishing_support", "0")));
                        pictureWallInfo.setOpposeCount(Integer
                                .valueOf(jsonObject.optString("wishing_oppose",
                                        "0")));
                        pictureWallInfo.setAction(jsonObject
                                .optString("action"));
                        if (jsonObject.optString("wishing_time", "").length() > 9) {
                            pictureWallInfo.setMonth(jsonObject.optString(
                                    "wishing_time").substring(5, 7));
                            pictureWallInfo.setDay(jsonObject.optString(
                                    "wishing_time").substring(8, 10));
                        }
                        list.add(pictureWallInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 客服中心在线咨询
     */
    public static List<OnlineConsultInfo> onlineConsultInfos(Context context) {
        List<OnlineConsultInfo> list = new ArrayList<OnlineConsultInfo>();
        OnlineConsultInfo onlineConsultInfo = null;

        onlineConsultInfo = new OnlineConsultInfo();
        onlineConsultInfo.setHeadShotBitmap(getBitMap(context,
                R.drawable.imgloading));
        onlineConsultInfo.setContent("您好，请问有什么问题呢？");
        onlineConsultInfo.setSelfContent(false);
        list.add(onlineConsultInfo);

        return list;
    }

    /**
     * 客服中心问题类型
     */
    public static List<CustomInfo> getCustomTitleInfos(String urlString) {
        InputStream is = null;
        CustomInfo customInfo = null;
        List<CustomInfo> list = new ArrayList<CustomInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject2.optJSONArray("type");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        customInfo = new CustomInfo();
                        customInfo.setId(jsonObject.optString("kefuqtype_id"));
                        customInfo.setType(jsonObject
                                .optString("kefuqtype_name"));
                        list.add(customInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 客服中心问题和回答
     */
    public static List<CustomInfo> getCustomInfos(String urlString) {
        InputStream is = null;
        CustomInfo customInfo = null;
        List<CustomInfo> list = new ArrayList<CustomInfo>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject2.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        customInfo = new CustomInfo();
                        customInfo.setQuestion(jsonObject
                                .optString("kefuq_question"));
                        customInfo.setAnswer(jsonObject
                                .optString("kefuq_answer"));
                        list.add(customInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取热门推荐标签内容数据
     */
    public static List<GoodsInfo> hotLabelData(Context context) {
        List<GoodsInfo> list = new ArrayList<GoodsInfo>();
        GoodsInfo goodsInfo = null;
        String[] goodsNames = new String[]{"三八妇女节", "六一儿童节", "十一黄金周"};
        Bitmap[] bitmaps = new Bitmap[]{
                getBitMap(context, R.drawable.article09),
                getBitMap(context, R.drawable.article06),
                getBitMap(context, R.drawable.article10)};
        int[] counts = new int[]{1175, 328, 265};

        for (int i = 0; i < goodsNames.length; i++) {
            goodsInfo = new GoodsInfo();
            goodsInfo.setGoodsIcon(bitmaps[i]);
            goodsInfo.setGoodsName(goodsNames[i]);
            goodsInfo.setCollection(counts[i]);
            list.add(goodsInfo);
        }
        return list;
    }

    /**
     * 获取小卖部订单数据
     */
    public static List<StoreOrderInfo> getOrderInfos(String urlString) {
        List<StoreOrderInfo> list = new ArrayList<StoreOrderInfo>();
        StoreOrderInfo storeOrderInfo = null;
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray orderDetail = jsonObject.optJSONArray("orderDetail");
                if (orderDetail != null) {
                    for (int i = 0; i < orderDetail.length(); i++) {
                        storeOrderInfo = new StoreOrderInfo();
                        JSONObject goods = orderDetail.getJSONObject(i);
                        storeOrderInfo.setIsPay(goods.optString("isPay"));
                        storeOrderInfo.setOrderId(goods.optString("orderId"));
                        storeOrderInfo.setOrderStatus(goods
                                .optInt("orderStatus"));
                        storeOrderInfo.setNeedPay(Float.valueOf(goods
                                .optString("needPay", "0")));
                        storeOrderInfo.setCreateTime(goods
                                .optString("createTime"));
                        storeOrderInfo.setFinishTime(goods
                                .optString("finishTime"));
                        storeOrderInfo.setUserId(goods.optString("userId"));
                        storeOrderInfo.setIsAppraise(goods.optString(
                                "isAppraise", "0"));
                        storeOrderInfo.setIsRefund(goods.optString("isRefund",
                                "0"));
                        storeOrderInfo.setShopid(goods.optString("shopid"));
                        storeOrderInfo.setIsSelf(goods.optString("isSelf"));
                        storeOrderInfo.setPayType(goods.optString("payType"));
                        storeOrderInfo.setCount(goods.optInt("goodsNum", 0));
                        storeOrderInfo.setShopPhone(goods
                                .optString("shopPhone"));
                        storeOrderInfo.setShopName(goods.optString("shopName"));
                        storeOrderInfo.setShopImg(goods.optString("shopImg"));

                        list.add(storeOrderInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取小卖部订单详情数据
     */
    public static StoreOrderInfo getOrderInfo(String urlString) {
        List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();
        StoreOrderInfo storeOrderInfo = new StoreOrderInfo();
        GoodsInfo goodsInfo = null;
        InputStream is = null;
        JSONObject jsonObject2 = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject goods = new JSONObject(jsonString);
                storeOrderInfo.setIsPay(goods.optString("isPay"));
                storeOrderInfo.setOrderId(goods.optString("orderId"));
                storeOrderInfo.setOrderNo(goods.optString("orderNo"));
                storeOrderInfo.setCount(goods.optInt("goodsNum", 0));
                storeOrderInfo.setOrderStatus(goods.optInt("orderStatus"));
                storeOrderInfo.setNeedPay(Float.valueOf(goods.optString(
                        "needPay", "0")));
                storeOrderInfo.setTotalMoney(Float.valueOf(goods.optString(
                        "totalMoney", "0")));
                storeOrderInfo.setDeliverMoney(Float.valueOf(goods.optString(
                        "deliverMoney", "0")));
                storeOrderInfo.setCreateTime(goods.optString("createTime"));
                storeOrderInfo.setFinishTime(goods.optString("finishTime"));
                storeOrderInfo.setUserId(goods.optString("userId"));
                storeOrderInfo
                        .setIsAppraise(goods.optString("isAppraise", "0"));
                storeOrderInfo.setStar(goods.optInt("star", 0));
                storeOrderInfo.setShopid(goods.optString("shopid"));
                storeOrderInfo.setIsSelf(goods.optString("isSelf"));
                storeOrderInfo.setIsRefund(goods.optString("isRefund", "0"));
                storeOrderInfo.setPayType(goods.optString("payType"));
                storeOrderInfo.setCount(goods.optInt("goodsNum", 0));
                storeOrderInfo.setUserPhone(goods.optString("userPhone"));
                storeOrderInfo.setUserName(goods.optString("userName"));
                storeOrderInfo.setUserAddress(goods.optString("userAddress"));
                storeOrderInfo.setShopPhone(goods.optString("shopPhone"));
                storeOrderInfo.setShopName(goods.optString("shopName"));
                storeOrderInfo.setShopImg(goods.optString("shopImg"));
                storeOrderInfo.setDeliverMember(goods
                        .optString("DistributionMember"));
                storeOrderInfo.setDeliverPhone(goods
                        .optString("DistributionMemberP"));

                JSONArray Allgoods = goods.optJSONArray("Allgoods");
                if (Allgoods != null) {
                    for (int j = 0; j < Allgoods.length(); j++) {
                        jsonObject2 = Allgoods.getJSONObject(j);
                        goodsInfo = new GoodsInfo();
                        goodsInfo.setGoodsID(jsonObject2.optString("goodsId"));
                        goodsInfo.setCount(jsonObject2.optInt("goodsNums"));
                        goodsInfo.setGoodsName(jsonObject2
                                .optString("goodsName"));
                        goodsInfo.setCurrentPrice(Float.valueOf(jsonObject2
                                .optString("goodsPrice", "0")));
                        goodsInfo.setGoodsIconUrl(jsonObject2
                                .optString("goodsThums"));
                        goodsInfos.add(goodsInfo);
                    }
                }
                storeOrderInfo.setList(goodsInfos);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storeOrderInfo;
    }

    /**
     * 获取小卖部订单评价数据
     */
    public static StoreOrderInfo getOrderAppraiseInfo(String urlString) {
        List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();
        StoreOrderInfo storeOrderInfo = new StoreOrderInfo();
        GoodsInfo goodsInfo = null;
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject goods = new JSONObject(jsonString);
                storeOrderInfo.setShopid(goods.optString("shopid"));
                storeOrderInfo.setUserName(goods.optString("userName"));
                storeOrderInfo.setShopName(goods.optString("shopName"));
                storeOrderInfo.setShopPhone(goods.optString("shopPhone"));
                storeOrderInfo.setShopImg(goods.optString("shopImg"));

                JSONArray Allgoods = goods.optJSONArray("Allgoods");
                if (Allgoods != null) {
                    for (int j = 0; j < Allgoods.length(); j++) {
                        goodsInfo = new GoodsInfo();
                        goodsInfo.setGoodsName(Allgoods.getJSONObject(j)
                                .optString("goodsName"));
                        goodsInfos.add(goodsInfo);
                    }
                }
                storeOrderInfo.setList(goodsInfos);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storeOrderInfo;
    }

    /**
     * 获取礼物说订单数据
     */
    public static List<GiftOrderInfo> getGiftOrderInfos(String urlString) {
        List<GiftOrderInfo> list = new ArrayList<GiftOrderInfo>();
        GiftOrderInfo giftOrderInfo = null;
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray orderDetail = jsonObject.optJSONArray("orderDetail");
                if (orderDetail != null) {
                    for (int i = 0; i < orderDetail.length(); i++) {
                        giftOrderInfo = new GiftOrderInfo();
                        JSONObject goods = orderDetail.getJSONObject(i);
                        giftOrderInfo.setIsPay(goods.optInt("isPay", 0));
                        giftOrderInfo
                                .setOrderID(goods.optString("orderId", ""));
                        giftOrderInfo.setStatus(goods.optInt("orderStatus"));
                        giftOrderInfo.setNeedPrice(Float.valueOf(goods
                                .optString("needPay", "0")));
                        giftOrderInfo.setPriceNotEnough(Float.valueOf(goods
                                .optString("orderRprice", "0")));
                        giftOrderInfo.setNeedType(goods.optInt("needType", 0));
                        giftOrderInfo.setTime(goods.optString("createTime"));
                        giftOrderInfo.setUserAddress(goods
                                .optString("userAddress"));
                        giftOrderInfo.setRemarkShare(goods.optString(
                                "remarkShare", ""));

                        JSONObject Allgoods = goods.optJSONObject("Allgoods");
                        if (Allgoods != null) {
                            GiftOrderGoodsInfo giftOrderGoodsInfo = new GiftOrderGoodsInfo();
                            giftOrderGoodsInfo.setGoodsId(Allgoods
                                    .optString("goodsId"));
                            giftOrderGoodsInfo.setGoodsNums(Allgoods
                                    .optInt("goodsNums"));
                            giftOrderGoodsInfo.setGoodsPrice(Float
                                    .valueOf(Allgoods.optString("goodsPrice",
                                            "0")));
                            giftOrderGoodsInfo.setGoodsAttrId(Allgoods
                                    .optString("goodsAttrId"));
                            giftOrderGoodsInfo.setGoodsAttrName(Allgoods
                                    .optString("goodsAttrName"));
                            giftOrderGoodsInfo.setGoodsName(Allgoods
                                    .optString("goodsName"));
                            giftOrderGoodsInfo.setGoodsThums(Allgoods
                                    .optString("goodsThums"));
                            giftOrderInfo
                                    .setGiftOrderGoodsInfo(giftOrderGoodsInfo);
                        }

                        list.add(giftOrderInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取礼物盒子凑一凑详情数据
     */
    public static GiftTogetherInfo getGiftTogetherInfo(String urlString) {
        List<GiftTogetherFriendInfo> list = new ArrayList<GiftTogetherFriendInfo>();
        GiftTogetherInfo giftTogetherInfo = new GiftTogetherInfo();
        GiftTogetherFriendInfo friendInfo = null;
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                giftTogetherInfo.setNeedPay(Float.valueOf(jsonObject.optString(
                        "needPay", "0")));
                giftTogetherInfo.setMoneyNotEnough(Float.valueOf(jsonObject
                        .optString("still", "0")));
                JSONArray replyList = jsonObject.optJSONArray("replyList");
                if (replyList != null) {
                    for (int i = 0; i < replyList.length(); i++) {
                        friendInfo = new GiftTogetherFriendInfo();
                        JSONObject friend = replyList.getJSONObject(i);
                        friendInfo.setHeadShot(friend
                                .optString("reply_img", ""));
                        friendInfo.setName(friend.optString("reply_name", ""));
                        friendInfo.setMoney(Float.valueOf(friend.optString(
                                "reply_moneys", "0")));
                        friendInfo.setContent(friend.optString(
                                "reply_contents", ""));
                        friendInfo.setTime(friend
                                .optString("reply_addtime", ""));
                        list.add(friendInfo);
                    }
                }
                giftTogetherInfo.setList(list);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return giftTogetherInfo;
    }

    /**
     * 获取礼物说订单详情数据
     */
    public static GiftOrderInfo getGiftOrderDetailInfo(String urlString) {
        GiftOrderInfo giftOrderInfo = new GiftOrderInfo();
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject goods = new JSONObject(jsonString);
            giftOrderInfo.setIsPay(goods.optInt("isPay"));
            giftOrderInfo.setOrderID(goods.optString("orderId"));
            giftOrderInfo.setOrderNO(goods.optString("orderNo"));
            giftOrderInfo.setStatus(goods.optInt("orderStatus"));
            giftOrderInfo.setNeedPrice(Float.valueOf(goods.optString("needPay",
                    "0")));
            giftOrderInfo.setPriceNotEnough(Float.valueOf(goods.optString(
                    "orderRprice", "0")));
            giftOrderInfo.setNeedType(goods.optInt("needType"));
            giftOrderInfo.setTime(goods.optString("createTime"));
            giftOrderInfo.setDeliverMoney(Float.valueOf(goods.optString(
                    "deliverMoney", "0")));
            giftOrderInfo.setUserAddress(goods.optString("userAddress"));
            giftOrderInfo.setUserPhone(goods.optString("userPhone"));
            giftOrderInfo.setUserName(goods.optString("userName"));
            giftOrderInfo.setLogisticsName(goods.optString("logisticsName"));
            giftOrderInfo.setLogisticsNum(goods.optString("logisticsNum"));
            giftOrderInfo.setLogisticsComs(goods.optString("logisticsComs"));
            giftOrderInfo.setUserID(goods.optString("userId"));
            giftOrderInfo.setRemarkShare(goods.optString("remarkShare", ""));

            JSONObject Allgoods = goods.getJSONObject("Allgoods");
            GiftOrderGoodsInfo giftOrderGoodsInfo = new GiftOrderGoodsInfo();
            giftOrderGoodsInfo.setGoodsId(Allgoods.optString("goodsId"));
            giftOrderGoodsInfo.setGoodsNums(Allgoods.optInt("goodsNums"));
            giftOrderGoodsInfo.setGoodsPrice(Float.valueOf(Allgoods.optString(
                    "goodsPrice", "0")));
            giftOrderGoodsInfo
                    .setGoodsAttrId(Allgoods.optString("goodsAttrId"));
            giftOrderGoodsInfo.setGoodsAttrName(Allgoods
                    .optString("goodsAttrName"));
            giftOrderGoodsInfo.setGoodsName(Allgoods.optString("goodsName"));
            giftOrderGoodsInfo.setGoodsThums(Allgoods.optString("goodsThums"));

            giftOrderInfo.setGiftOrderGoodsInfo(giftOrderGoodsInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return giftOrderInfo;
    }

    /**
     * 获取礼物说订单送给ta
     */
    public static GiftOrderInfo getGiftOrderSend(String urlString) {
        GiftOrderInfo giftOrderInfo = new GiftOrderInfo();
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject goods = new JSONObject(jsonString);
            giftOrderInfo.setOrderID(goods.optString("orderId"));
            giftOrderInfo.setDeliverMoney(Float.valueOf(goods.optString(
                    "deliverMoney", "0")));
            giftOrderInfo.setUserName(goods.optString("userName"));
            giftOrderInfo.setUserID(goods.optString("userId"));
            giftOrderInfo.setRemarkShare(goods.optString("remarkShare", ""));

            JSONObject Allgoods = goods.getJSONObject("Allgoods");
            GiftOrderGoodsInfo giftOrderGoodsInfo = new GiftOrderGoodsInfo();
            giftOrderGoodsInfo.setGoodsId(Allgoods.optString("goodsId"));
            giftOrderGoodsInfo.setGoodsNums(Allgoods.optInt("goodsNums"));
            giftOrderGoodsInfo.setGoodsPrice(Float.valueOf(Allgoods.optString(
                    "goodsPrice", "0")));
            giftOrderGoodsInfo
                    .setGoodsAttrId(Allgoods.optString("goodsAttrId"));
            giftOrderGoodsInfo.setGoodsAttrName(Allgoods
                    .optString("goodsAttrName"));
            giftOrderGoodsInfo.setGoodsName(Allgoods.optString("goodsName"));
            giftOrderGoodsInfo.setGoodsThums(Allgoods.optString("goodsThums"));

            giftOrderInfo.setGiftOrderGoodsInfo(giftOrderGoodsInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return giftOrderInfo;
    }

    /**
     * 获取学校数据
     */
    public static List<CampusInfo> getCampusInfos(String urlString) {
        List<CampusInfo> list = new ArrayList<CampusInfo>();
        InputStream is = null;
        CampusInfo campusInfo = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    campusInfo = new CampusInfo();
                    campusInfo.setCampusName(jsonObject
                            .optString("school_name"));
                    campusInfo.setCampusID(jsonObject.optString("school_id"));
                    // campusInfo.setSchool_pic(jsonObject.optString("school_pic",
                    // ""));
                    // campusInfo.setSchool_picthumb(jsonObject.optString(
                    // "school_picthumb", ""));
                    list.add(campusInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取城市数据
     */
    public static List<CityInfo> getBossCityInfos(String urlString) {
        List<CityInfo> list = new ArrayList<CityInfo>();
        InputStream is = null;
        CityInfo cityInfo = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    cityInfo = new CityInfo();
                    cityInfo.setCity(jsonObject.optString("school_name"));
                    cityInfo.setCityId(jsonObject.optString("school_id"));
                    list.add(cityInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取搜索的学校数据
     */
    public static List<CampusInfo> getSearchCampusInfos(String urlString) {
        List<CampusInfo> list = new ArrayList<CampusInfo>();
        InputStream is = null;
        CampusInfo campusInfo = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject2;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray schoolList = jsonObject.optJSONArray("schoolList");
                if (schoolList != null) {
                    for (int i = 0; i < schoolList.length(); i++) {
                        jsonObject2 = schoolList.getJSONObject(i);
                        campusInfo = new CampusInfo();
                        campusInfo.setCampusName(jsonObject2
                                .optString("school_name"));
                        campusInfo.setCampusID(jsonObject2
                                .optString("school_id"));
                        // campusInfo.setSchool_pic(jsonObject2
                        // .optString("school_pic"));
                        // campusInfo.setSchool_picthumb(jsonObject2
                        // .optString("school_picthumb"));
                        list.add(campusInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取第一个分类的商品数据
     */
    public static List<GoodsInfo> getGoodsData01(String urlString) {
        InputStream is = null;
        GoodsInfo goodsInfo = null;
        List<GoodsInfo> list = new ArrayList<GoodsInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.optJSONArray("Allcat");
                if (jsonArray == null) {
                    return null;
                }
                JSONObject goodsObject = jsonArray.getJSONObject(0);
                JSONArray catgoodslist = goodsObject
                        .optJSONArray("catgoodslist");
                if (catgoodslist != null) {
                    for (int i = 0; i < catgoodslist.length(); i++) {
                        goodsInfo = new GoodsInfo();
                        JSONObject goods = catgoodslist.getJSONObject(i);
                        goodsInfo.setGoodsName(goods.optString("goodsName"));
                        goodsInfo.setGoodsID(goods.optString("goodsId"));
                        goodsInfo
                                .setGoodsIconUrl(goods.optString("goodsThums"));
                        goodsInfo.setGoodsDetailUrl01(goods.optString(
                                "goodsImg", ""));
                        goodsInfo.setGoodsDetailUrl02(goods.optString(
                                "goodsImg1", ""));
                        goodsInfo.setGoodsDetailUrl03(goods.optString(
                                "goodsImg2", ""));
                        goodsInfo.setSold(goods.optInt("sold", 0));
                        goodsInfo.setCurrentPrice(Float.valueOf(goods
                                .optString("shopPrice", "0")));
                        goodsInfo.setOriginalPrice(Float.valueOf(goods
                                .optString("marketPrice", "0")));
                        goodsInfo.setUnit(goods.optString("goodsUnit"));
                        goodsInfo.setStock(goods.optInt("goodsStock"));
                        goodsInfo.setCatID(goods.optString("shopCatId1"));
                        list.add(goodsInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取商品分类数据
     */
    public static List<CategoryInfo> getGoodsCategoryData(String urlString) {
        InputStream is = null;
        CategoryInfo categoryInfo = null;
        List<CategoryInfo> list = new ArrayList<CategoryInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.optJSONArray("Allcat");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        categoryInfo = new CategoryInfo();
                        JSONObject goods = jsonArray.getJSONObject(i);
                        categoryInfo
                                .setCategoryName(goods.optString("catName"));
                        categoryInfo.setCatID(goods.optString("catId"));
                        list.add(categoryInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取礼物说分类数据
     */
    public static List<List<GiftCategoryInfo>> getGiftCategoryInfos(
            Context context, String urlString, int index) {
        InputStream is = null;
        GiftCategoryInfo giftCategoryInfo = null;
        List<GiftCategoryInfo> list;
        List<List<GiftCategoryInfo>> lists = new ArrayList<List<GiftCategoryInfo>>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject category1 = jsonArray.getJSONObject(index);
                JSONArray children = category1.optJSONArray("children");
                if (children != null) {
                    for (int i = 0; i < children.length(); i++) {
                        list = new ArrayList<GiftCategoryInfo>();
                        JSONObject jsonObjectChildren = children
                                .getJSONObject(i);
                        String categorySec = jsonObjectChildren
                                .optString("catName");
                        JSONArray son = jsonObjectChildren.optJSONArray("son");
                        if (son != null) {
                            for (int j = 0; j < son.length(); j++) {
                                JSONObject jsonObject = son.getJSONObject(j);
                                giftCategoryInfo = new GiftCategoryInfo();
                                giftCategoryInfo.setId(jsonObject
                                        .optString("catId"));
                                giftCategoryInfo.setCategorySecond(categorySec);
                                giftCategoryInfo.setCategoryThird(jsonObject
                                        .optString("catName"));
                                giftCategoryInfo.setPicUrl(jsonObject
                                        .optString("catPicthums"));
                                list.add(giftCategoryInfo);
                            }
                            lists.add(list);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }

    /**
     * 获取礼物说一级分类数据
     */
    public static List<CategoryInfo> getGiftCategoryFirst(Context context,
                                                          String urlString) {
        InputStream is = null;
        CategoryInfo categoryInfo = null;
        List<CategoryInfo> list = new ArrayList<CategoryInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    categoryInfo = new CategoryInfo();
                    JSONObject category = jsonArray.getJSONObject(i);
                    categoryInfo.setCategoryName(category.optString("catName"));
                    list.add(categoryInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取礼物说二级分类数据
     */
    public static List<CategoryInfo> getGiftCategorySec(Context context,
                                                        String urlString, int index) {
        InputStream is = null;
        CategoryInfo categoryInfo = null;
        List<CategoryInfo> list = new ArrayList<CategoryInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject jsonObject1 = jsonArray.getJSONObject(index);
                JSONArray children = jsonObject1.optJSONArray("children");
                if (children != null) {
                    for (int i = 0; i < children.length(); i++) {
                        categoryInfo = new CategoryInfo();
                        JSONObject category = children.getJSONObject(i);
                        categoryInfo.setCategoryName(category
                                .optString("catName"));
                        list.add(categoryInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取礼物说商品列表
     */
    public static List<GiftGoodsInfo> getGiftGoodsListInfos(String urlString) {
        InputStream is = null;
        GiftGoodsInfo giftGoodsInfo = null;
        List<GiftGoodsInfo> list = new ArrayList<GiftGoodsInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONArray jsonArray = object.optJSONArray("Giftboxgoods");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        giftGoodsInfo = new GiftGoodsInfo();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        giftGoodsInfo.setGiftgoodsName(jsonObject
                                .optString("giftgoodsName"));
                        giftGoodsInfo.setGiftgoodsId(jsonObject
                                .optString("giftgoodsId"));
                        giftGoodsInfo.setGiftgoodsImg(jsonObject
                                .optString("giftgoodsImg"));
                        giftGoodsInfo.setShopPrice(Float.valueOf(jsonObject
                                .optString("shopPrice", "0")));
                        giftGoodsInfo.setCollect(jsonObject
                                .optInt("collect", 0));
                        list.add(giftGoodsInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取礼物说商品详情数据
     */
    public static GiftGoodsInfo getGiftGoodsInfos(String urlString) {
        InputStream is = null;
        GiftGoodsInfo giftGoodsInfo = new GiftGoodsInfo();
        GiftGoodsAttr giftGoodsAttr = null;
        GiftGoodsAttrVal giftGoodsAttrVal = null;
        List<GiftGoodsAttr> attrs = new ArrayList<GiftGoodsAttr>();
        List<GiftGoodsAttrVal> attrVals = null;
        List<String> imgs = new ArrayList<String>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONObject goodsDetail = object.getJSONObject("goodsDetail");
                JSONArray attrkv = object.optJSONArray("attrkv");
                giftGoodsInfo.setGiftgoodsId(goodsDetail
                        .optString("giftgoodsId"));
                giftGoodsInfo.setGiftgoodsSn(goodsDetail
                        .optString("giftgoodsSn"));
                giftGoodsInfo.setGiftgoodsName(goodsDetail
                        .optString("giftgoodsName"));
                giftGoodsInfo.setGiftgoodsImg01(goodsDetail
                        .optString("giftgoodsImg"));
                giftGoodsInfo.setGiftgoodsImg02(goodsDetail
                        .optString("giftgoodsImg1"));
                giftGoodsInfo.setGiftgoodsImg03(goodsDetail
                        .optString("giftgoodsImg2"));
                giftGoodsInfo.setMarketPrice(Float.valueOf(goodsDetail
                        .optString("marketPrice", "0")));
                giftGoodsInfo.setShopPrice(Float.valueOf(goodsDetail.optString(
                        "shopPrice", "0")));
                giftGoodsInfo.setDeliverMoney(Float.valueOf(goodsDetail.optString(
                        "giftgoodsFreight", "0")));
                giftGoodsInfo.setGiftgoodsStock(goodsDetail.optInt(
                        "giftgoodsStock", 0));
                giftGoodsInfo.setGiftgoodsSold(goodsDetail.optInt(
                        "giftgoodsSold", 0));
                giftGoodsInfo.setCollect(goodsDetail.optInt("collect", 0));
                JSONArray giftgoodsdetail = goodsDetail
                        .optJSONArray("giftgoodsdetail");
                if (giftgoodsdetail != null) {
                    for (int i = 0; i < giftgoodsdetail.length(); i++) {
                        imgs.add(giftgoodsdetail.get(i).toString());
                    }
                }
                giftGoodsInfo.setGiftgoodsdetail(imgs);
                if (attrkv != null) {
                    for (int i = 0; i < attrkv.length(); i++) {
                        giftGoodsAttr = new GiftGoodsAttr();
                        attrVals = new ArrayList<GiftGoodsAttrVal>();
                        JSONObject jsonObject = attrkv.getJSONObject(i);
                        giftGoodsAttr.setAttrName(jsonObject
                                .optString("attrkey_name"));
                        JSONArray attrval = jsonObject.optJSONArray("attrval");
                        if (attrval != null) {
                            for (int j = 0; j < attrval.length(); j++) {
                                giftGoodsAttrVal = new GiftGoodsAttrVal();
                                JSONObject jsonObject2 = attrval
                                        .getJSONObject(j);
                                giftGoodsAttrVal.setAttrSymbol(Integer
                                        .valueOf(jsonObject2.optString(
                                                "attrval_symbol", "0")));
                                giftGoodsAttrVal.setAttrValue(jsonObject2
                                        .optString("attrval_value"));
                                giftGoodsAttrVal.setAttrPic(jsonObject2
                                        .optString("attrval_pic"));
                                attrVals.add(giftGoodsAttrVal);
                            }
                        }
                        giftGoodsAttr.setList(attrVals);
                        attrs.add(giftGoodsAttr);
                    }
                }
                giftGoodsInfo.setGoodsAttr(attrs);
                giftGoodsInfo.setCollectID(object.optString("cid", "0"));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return giftGoodsInfo;
    }

    /**
     * 获取礼物说商品详情数据
     */
    public static List<GiftGoodsStockInfo> getGiftGoodsStocks(String urlString) {
        InputStream is = null;
        GiftGoodsStockInfo giftGoodsStockInfo = null;
        List<GiftGoodsStockInfo> list = new ArrayList<GiftGoodsStockInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONArray goodsStock = object.optJSONArray("goodsStock");
                if (goodsStock != null) {
                    for (int i = 0; i < goodsStock.length(); i++) {
                        giftGoodsStockInfo = new GiftGoodsStockInfo();
                        JSONObject jsonObject = goodsStock.getJSONObject(i);
                        giftGoodsStockInfo.setAttrSymbol(jsonObject
                                .optString("sku_symbol_path"));
                        giftGoodsStockInfo
                                .setId(jsonObject.optString("sku_id"));
                        giftGoodsStockInfo.setPrice(Float.valueOf(jsonObject
                                .optString("sku_price", "0")));
                        giftGoodsStockInfo.setStock(jsonObject
                                .optInt("sku_stock"));
                        list.add(giftGoodsStockInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取小卖部商品信息
     */
    public static List<GoodsInfo> getGoodsInfoData(String urlString) {
        InputStream is = null;
        GoodsInfo goodsInfo = null;
        List<GoodsInfo> list = new ArrayList<GoodsInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject2;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray catgoodslist = jsonObject
                        .optJSONArray("catgoodslist");
                if (catgoodslist != null) {
                    for (int i = 0; i < catgoodslist.length(); i++) {
                        jsonObject2 = catgoodslist.optJSONObject(i);
                        goodsInfo = new GoodsInfo();
                        goodsInfo.setGoodsName(jsonObject2
                                .optString("goodsName"));
                        goodsInfo.setGoodsID(jsonObject2.optString("goodsId"));
                        goodsInfo.setGoodsIconUrl(jsonObject2
                                .optString("goodsThums"));
                        goodsInfo.setGoodsDetailUrl01(jsonObject2.optString(
                                "goodsImg", ""));
                        goodsInfo.setGoodsDetailUrl02(jsonObject2.optString(
                                "goodsImg1", ""));
                        goodsInfo.setGoodsDetailUrl03(jsonObject2.optString(
                                "goodsImg2", ""));
                        goodsInfo.setSold(jsonObject2.optInt("sold", 0));
                        goodsInfo.setCurrentPrice(Float.valueOf(jsonObject2
                                .optString("shopPrice", "0")));
                        goodsInfo.setOriginalPrice(Float.valueOf(jsonObject2
                                .optString("marketPrice", "0")));
                        goodsInfo.setUnit(jsonObject2.optString("goodsUnit",
                                "件"));
                        goodsInfo.setStock(jsonObject2.optInt("goodsStock", 0));
                        goodsInfo.setCatID(jsonObject2.optString("shopCatId1"));
                        list.add(goodsInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    /**
     * 设置商家信息
     */
    public static void setBusinessInfo(String urlString) {
        BusinessInfo.resetData();
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                BusinessInfo.shopID = jsonObject.optString("shopid");
                JSONObject shopInfo = jsonObject.getJSONObject("shopInfo");
                if (shopInfo == null) {
                    return;
                }
                BusinessInfo.shopName = shopInfo.optString("shopName", "");
                BusinessInfo.deliverTime = shopInfo.optString(
                        "deliveryCostTime", "");
                BusinessInfo.endTime = shopInfo
                        .optString("serviceEndTime", "0");
                BusinessInfo.expressFee = Float.valueOf(shopInfo.optString(
                        "deliveryMoney", "0"));
                BusinessInfo.phone = shopInfo.optString("shopTel", "");
                BusinessInfo.sendAtLeastMoney = Float.valueOf(shopInfo
                        .optString("deliveryFreeMoney", "0"));
                BusinessInfo.startTime = shopInfo.optString("serviceStartTime",
                        "0");
                BusinessInfo.address = shopInfo.optString("shopAddress", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断商家是否营业中
     */
    public static boolean getIsBusinessWork(String urlString) {
        InputStream is = null;
        boolean work = false;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject shopInfo = jsonObject.getJSONObject("shopInfo");
                if (shopInfo == null) {
                    return false;
                }
                if (shopInfo.optString("shopAtive").equals("1")) {
                    work = true;
                } else {
                    work = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return work;
    }

    /**
     * 获取消息信息
     */
    public static List<MessageInfo> getMessageListInfos(String urlString) {
        InputStream is = null;
        List<MessageInfo> list = new ArrayList<>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setMid(jsonObject.optString("msgId","0"));
                        messageInfo.setContent(jsonObject.optString("msgContent",""));
                        messageInfo.setPic(jsonObject.optString("pic",""));
                        messageInfo.setTime(jsonObject.optString("msgTime","0"));
                        messageInfo.setTitle(jsonObject.optString("class",""));
                        messageInfo.setType(jsonObject.optString("msgType","0"));
                        messageInfo.setStatus(jsonObject.optString("msgStatus","1"));
                        list.add(messageInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取消息信息
     */
    public static List<MessageInfo> getMessageDetailListInfos(String urlString) {
        InputStream is = null;
        List<MessageInfo> list = new ArrayList<>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject2.optJSONArray("msgList");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setMid(jsonObject.optString("msgId","0"));
                        messageInfo.setContent(jsonObject.optString("msgContent",""));
                        messageInfo.setOid(jsonObject.optString("oid","0"));
                        messageInfo.setType(jsonObject.optString("type","0"));
                        messageInfo.setTime(jsonObject.optString("msgTime","0"));
                        messageInfo.setOrderNumber(jsonObject.optString("orderNo","0"));
                        messageInfo.setCid(jsonObject.optString("cid","0"));
                        messageInfo.setAid(jsonObject.optString("id","0"));
                        messageInfo.setClassify(jsonObject.optString("classify","0"));
                        messageInfo.setStatus(jsonObject.optString("msgStatus","1"));
                        list.add(messageInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取收获地址信息
     */
    public static List<AddressInfo> getAddressInfos(String urlString) {
        InputStream is = null;
        AddressInfo addressInfo = null;
        List<AddressInfo> list = new ArrayList<AddressInfo>();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    addressInfo = new AddressInfo();
                    addressInfo.setUserId(jsonObject.optString("userId"));
                    addressInfo.setAddressId(jsonObject.optString("addressId"));
                    addressInfo.setUserName(jsonObject.optString("userName"));
                    // addressInfo.setSex(jsonObject.optString("userSex"));
                    addressInfo.setUserPhone(jsonObject.optString("userPhone"));
                    // addressInfo.setPostcode(jsonObject.optString("postCode"));
                    addressInfo.setAddress(jsonObject.optString("address"));
                    addressInfo.setIsDefault(jsonObject.optString("isDefault"));
                    addressInfo.setSchool(jsonObject.optString("schoolName"));
                    addressInfo.setSid(jsonObject.optString("sid"));
                    addressInfo.setProvince(jsonObject.optString("province"));
                    addressInfo.setPid(jsonObject.optString("areaId1"));
                    addressInfo.setCity(jsonObject.optString("city"));
                    addressInfo.setCid(jsonObject.optString("areaId2"));
                    addressInfo.setArea(jsonObject.optString("district"));
                    addressInfo.setAid(jsonObject.optString("areaId3"));
                    list.add(addressInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    /**
     * 获取我的信用信息
     */
    public static MyCreditInfo getMyCreditInfo(String urlString) {
        InputStream is = null;
        MyCreditInfo myCreditInfo = new MyCreditInfo();
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                myCreditInfo.setMoney(Integer.valueOf(jsonObject.optString(
                        "dis", "0")));
                myCreditInfo.setRepaymentThis(jsonObject.optInt("thismoney"));
                myCreditInfo.setRepaymentNext(jsonObject.optInt("nextmoney"));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myCreditInfo;

    }

    /**
     * 获取借款记录信息
     */
    public static List<BorrowHistoryInfo> getBorrowHistoryInfos(String urlString) {
        InputStream is = null;
        List<BorrowHistoryInfo> list = new ArrayList<BorrowHistoryInfo>();
        BorrowHistoryInfo borrowHistoryInfo = null;
        JSONObject jsonObject2 = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jielist = jsonObject.optJSONArray("jielist");
                if (jielist != null) {
                    for (int i = 0; i < jielist.length(); i++) {
                        borrowHistoryInfo = new BorrowHistoryInfo();
                        jsonObject2 = jielist.getJSONObject(i);
                        if (jsonObject2.optString("type").equals("1")) {
                            borrowHistoryInfo.setMoney(1000);
                        }
                        if (jsonObject2.optString("auditing").equals("0")) {
                            borrowHistoryInfo.setStatus("审核中");
                        } else if (jsonObject2.optString("auditing")
                                .equals("1")) {
                            borrowHistoryInfo.setStatus("通过");
                        } else if (jsonObject2.optString("auditing")
                                .equals("2")) {
                            borrowHistoryInfo.setStatus("未通过");
                        }
                        borrowHistoryInfo.setTime(MyUtils.getSqlDate(Long
                                .valueOf(jsonObject2.optString("stime", "0"))));
                        if (jsonObject2.optString("monthly").equals("1")) {
                            borrowHistoryInfo.setWay("99*12月");
                        }
                        list.add(borrowHistoryInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取还款账单信息
     */
    public static List<RepaymentInfo> getRepaymentInfos(String urlString) {
        InputStream is = null;
        List<RepaymentInfo> list = new ArrayList<RepaymentInfo>();
        RepaymentInfo repaymentInfos = null;
        JSONObject jsonObject2 = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.optJSONArray("list");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        repaymentInfos = new RepaymentInfo();
                        jsonObject2 = jsonArray.getJSONObject(i);
                        repaymentInfos.setPeriods(Integer.valueOf(jsonObject2
                                .optString("periods", "0")));
                        repaymentInfos.setTime(jsonObject2.optString("hktime"));
                        repaymentInfos.setMoney(Float.valueOf(jsonObject2
                                .optString("hkmoney", "0")));
                        if (jsonObject2.optString("hkstatus").equals("0")) {
                            repaymentInfos.setStatus("未还");
                        } else if (jsonObject2.optString("hkstatus")
                                .equals("1")) {
                            repaymentInfos.setStatus("已还");
                        } else if (jsonObject2.optString("hkstatus")
                                .equals("2")) {
                            repaymentInfos.setStatus("过期未还");
                        }
                        list.add(repaymentInfos);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取提现信息
     */
    public static List<CashoutInfo> getCashoutInfos(String urlString) {
        List<CashoutInfo> list = new ArrayList<CashoutInfo>();
        InputStream is = null;
        CashoutInfo cashoutInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray txlist = jsonObject2.optJSONArray("txlist");
                if (txlist != null) {
                    for (int i = 0; i < txlist.length(); i++) {
                        cashoutInfo = new CashoutInfo();
                        jsonObject = txlist.getJSONObject(i);
                        cashoutInfo.setAccount(jsonObject.optString("type"));
                        cashoutInfo.setMoney(jsonObject.optString("money", "0"));
                        cashoutInfo.setStatus(jsonObject.optString("txstatus"));
                        cashoutInfo.setTime(MyUtils.getSqlDateLong(Long
                                .valueOf(jsonObject.optString("txtime", "0"))));
                        cashoutInfo.setNum(jsonObject.optString("num"));
                        list.add(cashoutInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取消费记录信息
     */
    public static List<ConsumeInfo> getConsumeInfos(String urlString) {
        List<ConsumeInfo> list = new ArrayList<ConsumeInfo>();
        InputStream is = null;
        ConsumeInfo consumeInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("infolist");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        consumeInfo = new ConsumeInfo();
                        jsonObject = infolist.getJSONObject(i);
                        consumeInfo.setType(jsonObject.optString("actionType"));
                        consumeInfo.setMoney(jsonObject.optString("rmoney", "0"));
                        consumeInfo.setMoneyChange(jsonObject.optString("money",
                                "0"));
                        consumeInfo.setTime(jsonObject.optString("time"));
                        list.add(consumeInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业版消费记录信息
     */
    public static List<ConsumeInfo> getBossConsumeInfos(String urlString) {
        List<ConsumeInfo> list = new ArrayList<ConsumeInfo>();
        InputStream is = null;
        ConsumeInfo consumeInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("infolist");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        consumeInfo = new ConsumeInfo();
                        jsonObject = infolist.getJSONObject(i);
                        consumeInfo.setType(jsonObject.optString("orgmoney_type"));
                        consumeInfo.setMoney(jsonObject.optString(
                                "orgmoney_rmoney", "0"));
                        consumeInfo.setMoneyChange(jsonObject.optString(
                                "orgmoney_money", "0"));
                        consumeInfo.setTime(jsonObject.optString("orgmoney_time"));
                        list.add(consumeInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取积分记录信息
     */
    public static List<PointInfo> getPointInfos(String urlString) {
        List<PointInfo> list = new ArrayList<PointInfo>();
        InputStream is = null;
        PointInfo pointInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("infolist");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        pointInfo = new PointInfo();
                        jsonObject = infolist.getJSONObject(i);
                        pointInfo.setType(jsonObject.optString("type"));
                        pointInfo.setPoint(jsonObject.optInt("totalscore"));
                        pointInfo.setPointChange(jsonObject.optInt("score"));
                        pointInfo.setTime(jsonObject.optString("time"));
                        list.add(pointInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取积分记录信息
     */
    public static List<PointInfo> getBossPointInfos(String urlString) {
        List<PointInfo> list = new ArrayList<PointInfo>();
        InputStream is = null;
        PointInfo pointInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("infolist");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        pointInfo = new PointInfo();
                        jsonObject = infolist.getJSONObject(i);
                        pointInfo.setType(jsonObject.optString("orgscore_type"));
                        pointInfo
                                .setPoint(jsonObject.optInt("orgscore_totalscore"));
                        pointInfo.setPointChange(jsonObject
                                .optInt("orgscore_score"));
                        pointInfo.setTime(jsonObject.optString("orgscore_time"));
                        list.add(pointInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取我的收藏
     */
    public static List<MyCollectInfo> getMyCollectInfos(String urlString) {
        List<MyCollectInfo> list = new ArrayList<MyCollectInfo>();
        InputStream is = null;
        MyCollectInfo myCollectInfo = null;
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("collectlist");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        myCollectInfo = new MyCollectInfo();
                        jsonObject = infolist.getJSONObject(i);
                        myCollectInfo.setId(jsonObject.optString("lid"));
                        myCollectInfo.setName(jsonObject.optString("name"));
                        myCollectInfo.setPicUrl(jsonObject.optString("img"));
                        myCollectInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        list.add(myCollectInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取邀请好友或社团个数
     */
    public static int getInviteCount(String urlString) {
        InputStream is = null;
        int count=0;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                count = jsonObject2.optInt("count",0);
            } catch (JSONException e) {
                e.printStackTrace();
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 获取邀请好友列表
     */
    public static List<UserInfo> getInviteUserInfos(String urlString) {
        List<UserInfo> list = new ArrayList<UserInfo>();
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("list");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        UserInfo userInfo = new UserInfo();
                        JSONObject jsonObject = infolist.getJSONObject(i);
                        userInfo.setUid(jsonObject.optString("id"));
                        userInfo.setName(jsonObject.optString("name"));
                        userInfo.setPhone(jsonObject.optString("phone"));
                        userInfo.setHeadShot(jsonObject
                                .optString("photo"));
                        userInfo.setAgent_status(jsonObject
                                .optInt("agent_status",1));
                        list.add(userInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    /**
     * 获取邀请社团列表
     */
    public static List<CommunityBasicInfo> getInviteCommInfos(String urlString) {
        List<CommunityBasicInfo> list = new ArrayList<CommunityBasicInfo>();
        InputStream is = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray infolist = jsonObject2.optJSONArray("list");
                if (infolist != null) {
                    for (int i = 0; i < infolist.length(); i++) {
                        CommunityBasicInfo commInfo = new CommunityBasicInfo();
                        JSONObject jsonObject = infolist.getJSONObject(i);
                        commInfo.setId(jsonObject.optString("id"));
                        commInfo.setCommunityName(jsonObject.optString("name"));
                        commInfo.setHeadthumb(jsonObject
                                .optString("photo"));
                        list.add(commInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /**
     * 获取公司简介
     */
    public static BossCompanySummaryInfo getBossCompanySummaryInfo(
            String urlString) {
        InputStream is = null;
        BossCompanySummaryInfo summaryInfo = new BossCompanySummaryInfo();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                jsonObject = jsonObject2.getJSONObject("list");
                if (jsonObject != null) {
                    summaryInfo.setAddress(jsonObject
                            .optString("company_address"));
                    summaryInfo
                            .setCompany(jsonObject.optString("company_name"));
                    summaryInfo.setFlag(jsonObject.optInt("company_flag"));
                    summaryInfo.setFrame(jsonObject.optString("company_frame"));
                    summaryInfo.setHost(jsonObject.optString("company_url"));
                    summaryInfo.setLogo(jsonObject
                            .optString("company_logo", ""));
                    summaryInfo.setProperty(jsonObject
                            .optString("company_property"));
                    summaryInfo.setSize(jsonObject.optString("company_size"));
                    summaryInfo.setSummary(jsonObject
                            .optString("company_profile"));
                    summaryInfo.setMoney(jsonObject
                            .optString("company_zanmoney"));
                    summaryInfo.setCount(jsonObject.optInt("company_zantime"));
                    summaryInfo.setState(jsonObject.optInt("state",0));
                    summaryInfo.setBgThumb(jsonObject
                            .optString("company_background"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return summaryInfo;
    }

    /**
     * 获取活动搜索类型名称
     */
    public static List<CampusInfo> getBossActiTypeInfos(String urlString) {
        InputStream is = null;
        CampusInfo campusInfo = null;
        List<CampusInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray cartlist = jsonObject2.optJSONArray("cartlist");
                if (cartlist != null) {
                    for (int i = 0; i < cartlist.length(); i++) {
                        campusInfo = new CampusInfo();
                        jsonObject = cartlist.getJSONObject(i);
                        campusInfo.setCampusID(jsonObject
                                .optString("acti_carts_id"));
                        campusInfo.setCampusName(jsonObject
                                .optString("acti_carts_value"));
                        list.add(campusInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取活动列表
     */
    public static List<CommunityActivityInfo> getBossActivityInfos(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo activityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("actilist");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        activityInfo = new CommunityActivityInfo();
                        jsonObject = acti.getJSONObject(i);
                        activityInfo.setId(jsonObject.optString("acti_id"));
                        activityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        activityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb"));
                        activityInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(jsonObject
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setSupportCount(jsonObject.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setStatus(jsonObject.optInt("acti_status",
                                1));
                        activityInfo.setDate(jsonObject.optString("acti_time",
                                "0"));
                        activityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        activityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        activityInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        activityInfo.setFree(jsonObject.optString("acti_tian",
                                "0"));
                        list.add(activityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取更多活动列表
     */
    public static List<CommunityActivityInfo> getMoreActivityInfos(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo activityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        activityInfo = new CommunityActivityInfo();
                        jsonObject = acti.getJSONObject(i);
                        activityInfo.setId(jsonObject.optString("acti_id"));
                        activityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        activityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb"));
                        activityInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(jsonObject
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setSupportCount(jsonObject.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setStatus(jsonObject.optInt("acti_status",
                                1));
                        activityInfo.setDate(jsonObject.optString("acti_time",
                                "0"));
                        activityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        activityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        activityInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        activityInfo.setFree(jsonObject.optString("acti_tian",
                                "0"));
                        list.add(activityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业首页活动信息
     */
    public static List<CommunityActivityInfo> getBossHomeActivityInfos(
            String urlString) {
        InputStream is = null;
        CommunityActivityInfo activityInfo = null;
        List<CommunityActivityInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray acti = jsonObject2.optJSONArray("acti");
                if (acti != null) {
                    for (int i = 0; i < acti.length(); i++) {
                        activityInfo = new CommunityActivityInfo();
                        jsonObject = acti.getJSONObject(i);
                        activityInfo.setId(jsonObject.optString("acti_id"));
                        activityInfo.setActivityName(jsonObject
                                .optString("acti_name"));
                        activityInfo.setActiHeadShot(jsonObject
                                .optString("acti_headimgthumb"));
                        activityInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("acti_money", "0")));
                        activityInfo.setRaisedMoney(Float.valueOf(jsonObject
                                .optString("acti_hasmoney", "0")));
                        activityInfo.setSupportCount(jsonObject.optInt(
                                "acti_zhichi", 0));
                        activityInfo.setDate(jsonObject.optString("acti_time",
                                "0"));
                        activityInfo.setClassify(jsonObject
                                .optInt("acti_classify"));
                        activityInfo.setSchool(jsonObject
                                .optString("school_name"));
                        activityInfo.setStatus(jsonObject.optInt("acti_status",
                                1));
                        activityInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        activityInfo.setFree(jsonObject.optString("acti_tian",
                                "0"));
                        list.add(activityInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业首页需求信息
     */
    public static List<BossCommSupportInfo> getBossHomeNeedInfos(
            String urlString) {
        InputStream is = null;
        BossCommSupportInfo supportInfo = null;
        List<BossCommSupportInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray aneed = jsonObject2.optJSONArray("aneed");
                if (aneed != null) {
                    for (int i = 0; i < aneed.length(); i++) {
                        supportInfo = new BossCommSupportInfo();
                        jsonObject = aneed.getJSONObject(i);
                        supportInfo.setId(jsonObject.optString("aneed_id"));
                        supportInfo.setDemandName(jsonObject
                                .optString("aneed_name"));
                        supportInfo.setOffer(jsonObject
                                .optString("aneed_outline"));
                        supportInfo.setNeedMoney(Float.valueOf(jsonObject
                                .optString("aneed_money", "0")));
                        supportInfo.setRaiseMoney(Float.valueOf(jsonObject
                                .optString("aneed_hasmoney", "0")));
                        supportInfo.setHeadthumb(jsonObject
                                .optString("aneed_headimgthumb"));
                        supportInfo.setSupportCount(jsonObject.optInt(
                                "aneed_zhichi", 0));
                        supportInfo.setStatus(jsonObject.optInt("aneed_status",
                                1));
                        supportInfo.setOverTime(MyUtils.getSqlDate(jsonObject
                                .optLong("aneed_time", 0)));
                        supportInfo.setClassify(jsonObject
                                .optInt("aneed_classify"));
                        supportInfo.setSchool(jsonObject
                                .optString("school_name"));
                        supportInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        supportInfo.setFree(jsonObject.optString("aneed_tian",
                                "0"));
                        list.add(supportInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业首页企业信息
     */
    public static List<BossCompanySummaryInfo> getBossHomeBossInfos(
            String urlString) {
        InputStream is = null;
        BossCompanySummaryInfo bossInfos = null;
        List<BossCompanySummaryInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray org = jsonObject2.optJSONArray("org");
                if (org != null) {
                    for (int i = 0; i < org.length(); i++) {
                        bossInfos = new BossCompanySummaryInfo();
                        jsonObject = org.getJSONObject(i);
                        bossInfos.setId(jsonObject.optString("company_id"));
                        bossInfos.setCount(jsonObject.optInt("company_zantime",
                                0));
                        bossInfos.setMoney(jsonObject.optString(
                                "company_zanmoney", "0"));
                        bossInfos.setCompany(jsonObject
                                .optString("company_name"));
                        bossInfos.setLogo(jsonObject.optString(
                                "company_logoThumb", ""));
                        list.add(bossInfos);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业首页社团信息
     */
    public static List<CommunityBasicInfo> getBossHomeCommInfos(String urlString) {
        InputStream is = null;
        CommunityBasicInfo commInfo = null;
        List<CommunityBasicInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray org = jsonObject2.optJSONArray("comn");
                if (org != null) {
                    for (int i = 0; i < org.length(); i++) {
                        commInfo = new CommunityBasicInfo();
                        jsonObject = org.getJSONObject(i);
                        commInfo.setId(jsonObject.optString("community_id"));
                        commInfo.setHeadthumb(jsonObject
                                .optString("community_logothumb"));
                        commInfo.setCommunityName(jsonObject
                                .optString("community_name"));
                        commInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        commInfo.setSchool(jsonObject.optString("school_name"));
                        list.add(commInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业首页社团信息
     */
    public static List<CommunityBasicInfo> getBossCommListInfos(String urlString) {
        InputStream is = null;
        CommunityBasicInfo commInfo = null;
        List<CommunityBasicInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray comn = jsonObject2.optJSONArray("comn");
                if (comn != null) {
                    for (int i = 0; i < comn.length(); i++) {
                        commInfo = new CommunityBasicInfo();
                        jsonObject = comn.getJSONObject(i);
                        commInfo.setId(jsonObject.optString("community_id"));
                        commInfo.setName(jsonObject.optString("userName"));
                        commInfo.setHeadthumb(jsonObject
                                .optString("community_logothumb"));
                        commInfo.setCommunityName(jsonObject
                                .optString("community_name"));
                        commInfo.setCity(jsonObject.optString("areaName")
                                + jsonObject.optString("city"));
                        commInfo.setSchool(jsonObject.optString("school_name"));
                        list.add(commInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业赞助过的社团信息
     */
    public static List<CommunityBasicInfo> getBossMyCommListInfos(
            String urlString) {
        InputStream is = null;
        CommunityBasicInfo commInfo = null;
        List<CommunityBasicInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray comn = jsonObject2.optJSONArray("list");
                if (comn != null) {
                    for (int i = 0; i < comn.length(); i++) {
                        commInfo = new CommunityBasicInfo();
                        jsonObject = comn.getJSONObject(i);
                        commInfo.setId(jsonObject.optString("community_id"));
                        commInfo.setHeadthumb(jsonObject
                                .optString("community_logothumb"));
                        commInfo.setCommunityName(jsonObject
                                .optString("community_name"));
                        commInfo.setSchool(jsonObject.optString("school_name"));
                        list.add(commInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取企业关注的社团信息
     */
    public static List<CommunityBasicInfo> getBossInterestInfos(String urlString) {
        InputStream is = null;
        CommunityBasicInfo commInfo = null;
        List<CommunityBasicInfo> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            is = new URL(urlString).openStream();
            String jsonString = readStream(is);
            try {
                JSONObject jsonObject2 = new JSONObject(jsonString);
                JSONArray root = jsonObject2.optJSONArray("root");
                if (root != null) {
                    for (int i = 0; i < root.length(); i++) {
                        commInfo = new CommunityBasicInfo();
                        jsonObject = root.getJSONObject(i);
                        commInfo.setId(jsonObject.optString("cmmId"));
                        commInfo.setHeadthumb(jsonObject
                                .optString("userPhotoThums"));
                        commInfo.setCommunityName(jsonObject
                                .optString("userName"));
                        commInfo.setSchool(jsonObject.optString("school_name"));
                        list.add(commInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取图片bitmap,避免内存溢出
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        return BitmapFactory.decodeStream(context.getResources()
                .openRawResource(resId), null, opt);
    }

    /**
     * 通过InputStream解析网页返回的数据
     */
    public static String readStream(InputStream is) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is, "utf-8");
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
