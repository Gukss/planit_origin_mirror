import { useRecoilState } from 'recoil';
import React, { useEffect, useState, useRef } from 'react';

import * as StompJs from '@stomp/stompjs';
import SockJS from 'sockjs-client';

import Bbar from '../../../../common/bbar/Bbar';
import classes from './Place.module.scss';
import PlaceBox from './PlaceBox';
import { userMarkers } from '../../../../app/store';

function Place() {
  // todo: 전역으로 수정하기
  const client = useRef({});

  const [items, setItems] = useRecoilState(userMarkers);
  const [temp, setTemp] = useState('');

  // 여기 1 나중에 룸번호로 변경해야함
  const subscribe = async () => {
    console.log('subscribe');
    console.log('외않되');
    await client.current.subscribe('/sub/markers/1', ({ body }) => {
      // setMessages(messages => [...messages, JSON.parse(body)]);
      // console.log('subsub');
      // setItems(body);
      console.log(body);
    });
  };

  const publish = async message => {
    console.log('publish');
    console.log(message);
    if (!client.current.connected) {
      return;
    }

    await client.current.publish({
      destination: '/pub/markers',
      // 여기 1 나중에 룸번호로 변경해야함
      body: JSON.stringify({ roomId: 1, storegeItemList: message }),
    });
  };

  const connect = () => {
    console.log('connect');
    client.current = new StompJs.Client({
      // brokerURL: "ws://localhost:8080/ws-stomp/websocket", // 웹소켓 서버로 직접 접속
      webSocketFactory: () =>
        new SockJS('https://i8b202.p.ssafy.io/api/ws-stomp'), // proxy를 통한 접속
      connectHeaders: {},
      debug: str => {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('커넥트 되는 시점');
        subscribe();
      },
      onStompError: frame => {
        console.error(frame);
      },
    });

    client.current.activate();
  };

  const disconnect = () => {
    console.log('disconnect');
    client.current.deactivate();
  };

  // 제출한 메세지를 state에 담는 함수
  const submitMessage = e => {
    console.log('submitMessage');
    if (e.keyCode === 13) {
      e.preventDefault();

      publish(e.target.value);

      e.target.value = '';
    }
  };

  useEffect(() => {
    connect();
    return () => disconnect();
  }, []);

  const pubfunc = async () => {
    await publish(items);
  };

  useEffect(() => {
    pubfunc();
    console.log(items);
  }, [items]);

  console.log('여기', items);

  const itemHotels = items.filter(item => item.category === 'AD5');
  const itemRestaurants = items.filter(item => item.category === 'FD6');
  const itemSpots = items.filter(item => item.category === 'AT4');
  const itemCafes = items.filter(item => item.category === 'CE7');
  const itemElses = items.filter(
    item =>
      item.category !== 'AD5' &&
      item.category !== 'FD6' &&
      item.category !== 'AT4' &&
      item.category !== 'CE7'
  );

  return (
    <Bbar>
      <div className={classes.place_title}>
        <div className={classes.title_title}>장소 보관함</div>
        <p>
          가고 싶은 장소를 담고 장소를 클릭하여 일정에 들어갈 확정 장소를
          선택하세요! 색깔을 눌러 해당 위치로 이동하세요!
        </p>
      </div>
      <div className={classes.placebox_section}>
        {/* {boxTitles.map(boxTitle => {
          return <PlaceBox boxTitle={boxTitle} />;
        })} */}
        {/* {items.map((item, index) => (
          // if (item.category === 'AD5') {
          <PlaceBox key={item} boxTitle='숙소' item={item} />
          // } else if (item.category === 'FD6') {
          //   return <PlaceBox boxTtile='음식점' item={item} />;
          // }
        ))} */}
        <PlaceBox boxTitle='숙소' items={itemHotels} />
        <PlaceBox boxTitle='음식점' items={itemRestaurants} />
        <PlaceBox boxTitle='관광지' items={itemSpots} />
        <PlaceBox boxTitle='카페' items={itemCafes} />
        <PlaceBox boxTitle='기타' items={itemElses} />
      </div>
    </Bbar>
  );
}

export default Place;
