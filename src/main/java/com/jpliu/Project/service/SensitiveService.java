package com.jpliu.Project.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }

            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败", e.getMessage());
        }
    }

    //增加关键词
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);

            if (isSymbol(c)) {
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);

            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if (i == lineTxt.length() -1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    private class TrieNode {
        private boolean end = false;//表示是不是某一个敏感词的结尾

        //表示当前节点下 所有的节点
        private Map<Character, TrieNode> subNode = new HashMap<Character, TrieNode>();//表示节点数

        //加key
        public void addSubNode(Character key, TrieNode node) {
            subNode.put(key, node);
        }

        public TrieNode getSubNode(Character key) {
            return subNode.get(key);
        }

        public boolean isKeyWordEnd() {
            return end;
        }

        public void setKeyWordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    //是否为符号等
    private boolean isSymbol(char c) {
        int ic = (int) c;
        //东亚文字0x2E80 - 0x9FFF
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }



        StringBuilder result = new StringBuilder();

        String replacement = "***";
        TrieNode tempNode = rootNode;//树的节点
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);//讲position位置的char 取出来

            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);//在树里寻找是否有这个值

            if (tempNode == null) {
                result.append(text.charAt(position));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd()) {
                //发现敏感词
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }

        result.append(text.substring(begin));

        return result.toString();
    }

    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("hi 你好色 情"));
    }

}
