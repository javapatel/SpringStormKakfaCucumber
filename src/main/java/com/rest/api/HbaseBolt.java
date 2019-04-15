package com.rest.api;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HbaseBolt extends BaseRichBolt {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(HbaseBolt.class);

    @Autowired
    PaymentStoreService paymentStoreService;

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        LOG.info("--------------------------- MSG--------");
        String paymentId = input.getString(2);
        LOG.info(paymentId);
        //collector.emit(new Values(input.getString(0)));
        Payment payment = new Payment();
        payment.setPaymentDate(new Date().toString());
        payment.setPaymentFromParty("Ajay Patel");
        payment.setPaymentToParty("Prem");
        payment.setPaymentId(paymentId);
        paymentStoreService.store(payment);
        //collector.emit(new Values(input.getString(2)));
        //collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }
}