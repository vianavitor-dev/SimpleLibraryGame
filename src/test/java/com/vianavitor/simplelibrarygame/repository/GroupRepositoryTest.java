package com.vianavitor.simplelibrarygame.repository;

import com.vianavitor.simplelibrarygame.model.Group;
import com.vianavitor.simplelibrarygame.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Student testStudent;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        testStudent = new Student("group_creator", "pass");
        testStudent.setName("Group Creator");
        studentRepository.save(testStudent);

        testGroup = new Group();
        testGroup.setName("Fantasy Book Club");
        testGroup.setStudent(testStudent);
        groupRepository.save(testGroup);
    }

    @Test
    void shouldSaveGroup() {
        Group newGroup = new Group();
        newGroup.setName("Sci-Fi Readers");
        newGroup.setStudent(testStudent);

        Group saved = groupRepository.save(newGroup);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Sci-Fi Readers");
        assertThat(saved.getStudent().getId()).isEqualTo(testStudent.getId());
    }

    @Test
    void shouldFindGroupByStudent() {
        List<Group> groups = groupRepository.findByStudent(testStudent);

        assertThat(groups).hasSize(1);
        assertThat(groups.get(0).getName()).isEqualTo("Fantasy Book Club");
    }

    @Test
    void shouldFindMultipleGroupsForStudent() {
        Group secondGroup = new Group();
        secondGroup.setName("Mystery Club");
        secondGroup.setStudent(testStudent);
        groupRepository.save(secondGroup);

        List<Group> groups = groupRepository.findByStudent(testStudent);
        assertThat(groups).hasSize(2);
        assertThat(groups).extracting(Group::getName)
                .containsExactlyInAnyOrder("Fantasy Book Club", "Mystery Club");
    }

    @Test
    void shouldReturnEmptyListWhenStudentHasNoGroups() {
        Student newStudent = new Student("no_groups", "pass");
        studentRepository.save(newStudent);

        List<Group> groups = groupRepository.findByStudent(newStudent);
        assertThat(groups).isEmpty();
    }

    @Test
    void shouldUpdateGroupName() {
        testGroup.setName("Updated Fantasy Club");
        groupRepository.save(testGroup);

        Group updated = groupRepository.findById(testGroup.getId()).get();
        assertThat(updated.getName()).isEqualTo("Updated Fantasy Club");
    }

    @Test
    void shouldDeleteGroup() {
        groupRepository.deleteById(testGroup.getId());

        assertThat(groupRepository.findById(testGroup.getId())).isEmpty();
    }

    @Test
    void shouldHandleMultipleStudentsWithGroups() {
        Student anotherStudent = new Student("another_creator", "pass");
        studentRepository.save(anotherStudent);

        Group anotherGroup = new Group();
        anotherGroup.setName("Another Club");
        anotherGroup.setStudent(anotherStudent);
        groupRepository.save(anotherGroup);

        List<Group> firstStudentGroups = groupRepository.findByStudent(testStudent);
        List<Group> secondStudentGroups = groupRepository.findByStudent(anotherStudent);

        assertThat(firstStudentGroups).hasSize(1);
        assertThat(secondStudentGroups).hasSize(1);
        assertThat(firstStudentGroups.get(0).getName()).isNotEqualTo(secondStudentGroups.get(0).getName());
    }
}