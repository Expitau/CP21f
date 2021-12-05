#include <iostream>

#ifndef LAB11_MONSTER_H
#define LAB11_MONSTER_H

using namespace std;

typedef int hp_t;  // using hp_t = int;
typedef int speed_t;

enum MonsterType {
    waterMon = 0,
    fireMon = 1,
    grassMon = 2,
};

class Monster {
private:
    static int num_monsters;
protected:
    string name;
    hp_t hp;
    hp_t damage;
    MonsterType type;
    MonsterType critical_to;
    int id;
    int speed;

public:
    Monster(string name, hp_t hp, hp_t damage, speed_t speed, MonsterType type, MonsterType critical_to);
    hp_t get_hp() const;
    MonsterType get_type() const;
    speed_t get_speed() const;

    void decrease_health(hp_t attack_damage);
    void attack(Monster *attacked_monster);
    virtual void critical_attack(Monster *attacked_monster);

    friend ostream& operator<<(ostream& os, const Monster& m);
};

class WaterMon : public Monster {
public:
    WaterMon();
    void critical_attack(Monster *attacked_monster);
};

class FireMon : public Monster {
public:
    FireMon();
    void critical_attack(Monster *attacked_monster);
};

class GrassMon : public Monster {
public:
    GrassMon();
    void critical_attack(Monster *attacked_monster);
};

#endif //LAB11_MONSTER_H
